package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void create(Film film);

    Film findById(int id);

    void update(Film film);

    void delete(Film film);

    List<Film> findAll();

    void contains(int id);
}
