package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        id++;
        film.setId(id);
        log.debug("Create film - {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilms(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ObjectNotFoundException("The movie with the id " + id + " was not found", "Film");
        }
        films.put(film.getId(), film);
        log.debug("Update film - {}", film);
        return film;
    }
}
