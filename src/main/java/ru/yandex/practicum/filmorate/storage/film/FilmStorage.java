package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void create(Film film);

    Film read(int id);

    void update(Film film);

    void delete(Film film);

    Collection<Film> findAll();
}
