package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film findById(int id);

    List<Film> findAll();

    Film update(Film film);

    void addLike(int idFilm, int userId);

    void removeLike(int idFilm, int userId);

    List<Film> getTopListFilm(int count);
}
