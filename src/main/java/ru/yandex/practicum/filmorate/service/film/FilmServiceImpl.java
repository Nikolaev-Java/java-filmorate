package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genres.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    @Override
    public Film create(Film model) {
        mpaRepository.findById(model.getMpa().id())
                .orElseThrow(() -> new InternalServerException("Mpa with id: " + model.getMpa().id() + " not foud"));
        if (!model.getGenres().isEmpty()) {
            List<Integer> filmsGenresIds = model.getGenres().stream()
                    .map(Genre::getId)
                    .toList();
            List<Genre> genreByIds = genreRepository.findByIds(filmsGenresIds);
            if (filmsGenresIds.size() != genreByIds.size()) {
                throw new InternalServerException("Genres with ids {" + filmsGenresIds + "} not found");
            }
        }
        filmRepository.create(model);
        log.debug("Create film - {}", model);
        return model;
    }

    @Override
    public Film findById(int id) {
        return filmRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Film with id " + id + " not found", "Film")
        );
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
        filmRepository.addLike(idFilm, userId);
    }

    @Override
    public void removeLike(int idFilm, int userId) {
        filmRepository.removeLike(idFilm, userId);
    }

    @Override
    public List<Film> getTopListFilm(int count) {
        return filmRepository.findTopPopularFilms(count);
    }
}
