package ru.yandex.practicum.filmorate.dal.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    void create(Film film);

    Film findById(int id);

    void update(Film film);

    void delete(Film film);

    List<Film> findAll();

    boolean contains(int id);
}
