package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService implements Services<Film> {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    @Override
    public Film create(Film model) {
        filmStorage.create(model);
        log.debug("Create film - {}", model);
        return model;
    }

    @Override
    public Film findById(int id) {
        return filmStorage.read(id);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film update(Film model) {
        filmStorage.update(model);
        log.debug("Update film - {}", model);
        return model;
    }

    public void addLike(int idFilm, int userId) {
        userStorage.read(userId);
        Film film = filmStorage.read(idFilm);
        film.addLike(userId);
    }

    public void removeLike(int idFilm, int usersId) {
        userStorage.read(usersId);
        filmStorage.read(idFilm).removeLike(usersId);
    }

    public List<Film> getTopListFilm(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> -1 * f1.getLikes().size() - f2.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
