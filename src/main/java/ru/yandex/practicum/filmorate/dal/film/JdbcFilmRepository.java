package ru.yandex.practicum.filmorate.dal.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Repository
public class JdbcFilmRepository implements FilmRepository {
    @Override
    public void create(Film film) {

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

    @Override
    public boolean contains(int id) {
        return false;
    }
}
