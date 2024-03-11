package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public void create(Film film) {
        calcId();
        film.setId(id);
        films.put(id, film);
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean contains(int id) {
        return films.containsKey(id);
    }

    @Override
    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("The movie with the id " + film.getId() + " was not found", "Film");
        }
        films.put(film.getId(), film);
    }

    @Override
    public Film findById(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("The movie with the id " + id + " was not found", "Film");
        }
        return films.get(id);
    }

    private void calcId() {
        this.id++;
    }
}
