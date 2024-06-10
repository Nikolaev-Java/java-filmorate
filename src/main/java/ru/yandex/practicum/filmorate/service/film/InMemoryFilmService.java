package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.film.FilmRepository;
import ru.yandex.practicum.filmorate.dal.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InMemoryFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Autowired
    public InMemoryFilmService(FilmRepository filmRepository, UserRepository userRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Film create(Film model) {
        filmRepository.create(model);
        log.debug("Create film - {}", model);
        return model;
    }

    @Override
    public Film findById(int id) {
        return filmRepository.findById(id);
    }

    @Override
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film update(Film model) {
        filmRepository.update(model);
        log.debug("Update film - {}", model);
        return model;
    }

    @Override
    public void addLike(int idFilm, int userId) {
        if (!userRepository.contains(userId)) {
            throw new NotFoundException(
                    String.format("You can't like it. The user with this %s was not found", userId), "User");
        }
        Film film = filmRepository.findById(idFilm);
        film.addLike(userId);
    }

    @Override
    public void removeLike(int idFilm, int userId) {
        if (!userRepository.contains(userId)) {
            throw new NotFoundException(
                    String.format("You can't delete a like. The user with this %s was not found", userId), "User");
        }
        filmRepository.findById(idFilm).removeLike(userId);
    }

    @Override
    public List<Film> getTopListFilm(int count) {
        return filmRepository.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
