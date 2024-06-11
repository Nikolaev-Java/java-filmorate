package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    @Override
    public void create(Film film) {
        String sql = "INSERT INTO film (film_name, description, release_date, duration, mpa_id) " +
                "VALUES (:name, :description, :release_date, :duration, :mpa_id)";

    }

    @Override
    public Film findById(int id) {
        return null;
    }

    @Override
    public void update(Film film) {

    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public List<Film> findAll() {
        return List.of();
    }

}
