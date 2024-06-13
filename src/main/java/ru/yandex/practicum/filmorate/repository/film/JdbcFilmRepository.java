package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.*;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final FilmResultSetExtractor filmResultSetExtractor;
    private final FilmRowMapper filmRowMapper;
    private final GenreResultExtractor genreResultExtractor;

    @Override
    public void create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO films (film_name, description, release_date, duration, mpa_id) " +
                "VALUES (:name, :description, :releaseDate, :duration, :mpaId)";
        jdbc.update(sql, getParamsMap(film), keyHolder);
        if (Objects.nonNull(keyHolder.getKey())) {
            film.setId(keyHolder.getKey().intValue());
        } else {
            throw new InternalServerException("Ошибка создания пользователя");
        }
        createFilmGenresRelation(film);
    }

    @Override
    public Optional<Film> findById(int id) {
        String queryGenres = "SELECT * FROM GENRES";
        Map<Integer, Genre> genreMap = jdbc.query(queryGenres, genreResultExtractor);
        String sql = "SELECT * FROM films LEFT JOIN PUBLIC.FILM_GENRES FG on films.FILM_ID = FG.FILM_ID " +
                "join PUBLIC.MPA M on M.MPA_ID = films.MPA_ID" +
                " WHERE films.FILM_ID = :id";
        try {
            Film result = jdbc.query(sql, Map.of("id", id), filmResultSetExtractor);
            if(result.getGenres()!=null && !result.getGenres().isEmpty()) {
                result.getGenres().forEach(genre -> genre.setName(genreMap.get(genre.getId()).getName()));
            }
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE FILMS SET " +
                "film_name = :name, " +
                "description = :description, " +
                "release_date = :releaseDate, " +
                "duration = :duration, " +
                "mpa_id = :mpaId " +
                "WHERE FILMS.FILM_ID = :id";
        int updateRow = jdbc.update(sql, getParamsMap(film));
        if (updateRow != 1) {
            throw new NotFoundException("Film not update","Film");
        }
        String queryDeleteGenreRelation = "DELETE FROM FILM_GENRES WHERE FILM_ID = :id";
        jdbc.update(queryDeleteGenreRelation, getParamsMap(film));
        createFilmGenresRelation(film);
    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public List<Film> findAll() {
        String queryGenres = "SELECT * FROM GENRES";
        Map<Integer, Genre> genreMap = jdbc.query(queryGenres, genreResultExtractor);
        String queryFilmGenres = "SELECT * FROM FILM_GENRES";
        List<GenresRelation> genresRelations = jdbc.query(queryFilmGenres, (rs, rowNum) ->
                new GenresRelation(rs.getInt("film_id"), rs.getInt("genre_id")));
        String queryFilms = "SELECT * FROM FILMS";
        List<Film> films = jdbc.query(queryFilms, filmRowMapper);
        films.forEach(film -> film.setGenres(getGenresFilms(film.getId(), genresRelations, genreMap)));
        return films;
    }

    @Override
    public List<Film> getTopPopularFilms(int count) {
        return List.of();
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "insert into likes (film_id, user_id) values (:filmId, :userId);";
        jdbc.update(sql, Map.of("filmId", filmId, "userId", userId));
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "delete from likes where film_id=:filmId and user_id=:userId";
        jdbc.update(sql, Map.of("filmId", filmId, "userId", userId));
    }

    @Override
    public List<Film> findTopPopularFilms(int count) {
        String sql = "SELECT FILMS.FILM_ID,FILMS.film_name,FILMS.DESCRIPTION,FILMS.RELEASE_DATE,FILMS.DURATION,FILMS.MPA_ID " +
                "FROM films " +
                "LEFT JOIN likes ON films.film_id=likes.film_id " +
                "GROUP BY films.film_id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT :count";
        return jdbc.query(sql,Map.of("count", count), filmRowMapper);
    }

    private record GenresRelation(int filmId, int genreId) {
    }

    private Set<Genre> getGenresFilms(int filmId, List<GenresRelation> genresRelations, Map<Integer, Genre> genresMap) {
        return genresRelations.stream()
                .filter(genresRelation -> genresRelation.filmId == filmId)
                .map(GenresRelation::genreId)
                .map(genresMap::get)
                .collect(Collectors.toSet());
    }

    private MapSqlParameterSource getParamsMap(Film film) {
        return new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpaId", film.getMpa().id())
                .addValue("id", film.getId());
    }

    private void createFilmGenresRelation(Film film) {
        List<Map<String, Integer>> genreId = film.getGenres().stream()
                .map(genre -> Map.of("genreId", genre.getId(),
                        "filmId", film.getId()))
                .collect(Collectors.toList());
        SqlParameterSource[] parameterSource =
                SqlParameterSourceUtils.createBatch(genreId);
        String sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ( :filmId, :genreId )";
        jdbc.batchUpdate(sql, parameterSource);
    }
}
