package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<Film> {

    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        Film film = null;
        Set<Genre> genres = new LinkedHashSet<>();
        while (rs.next()) {
            if (rs.getInt("genre_id") != 0) {
                genres.add(new Genre(rs.getInt("genre_id")));
            }
            if (film == null) {
                film = new Film();
                film.setId(rs.getInt("film_id"));
                film.setDescription(rs.getString("description"));
                film.setName(rs.getString("film_name"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getLong("duration"));
                film.setMpa(new Mpa(rs.getInt("mpa.mpa_id"), rs.getString("mpa.mpa_name")));
            }
        }
        if (film != null) {
            film.setGenres(genres);
        }
        return film;
    }
}
