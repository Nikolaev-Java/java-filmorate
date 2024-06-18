package ru.yandex.practicum.filmorate.repository.genres;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> findAll() {
        String sql = "select * from genres";
        return jdbc.query(sql, genreRowMapper);
    }

    @Override
    public Optional<Genre> findById(int id) {
        String sql = "select * from genres where genre_id = :id";
        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, Map.of("id", id), genreRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findByIds(List<Integer> ids) {
        String sql = "select * from genres where genre_id in (:ids)";
        return jdbc.query(sql, Map.of("ids", ids), genreRowMapper);
    }
}
