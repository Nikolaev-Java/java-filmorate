package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    void create(Film film);

    Optional<Film> findById(int id);

    void update(Film film);

    void delete(Film film);

    List<Film> findAll();

    List<Film> getTopPopularFilms(int count);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> findTopPopularFilms(int count);
}
