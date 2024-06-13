package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.mappers.GenreResultExtractor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcFilmRepository.class, FilmRowMapper.class, FilmResultSetExtractor.class, GenreResultExtractor.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmRepositoryTest {
    private final JdbcFilmRepository filmRepository;

    private static final int TEST_FILM01_ID = 1;
    private static final int TEST_FILM02_ID = 2;
    private static final int TEST_FILM03_ID = 3;
    private static final int TEST_FILM04_ID = 4;
    private static final String TEST_FILM01_NAME = "test01";
    private static final String TEST_FILM02_NAME = "test02";
    private static final String TEST_FILM03_NAME = "test03";
    private static final String TEST_FILM04_NAME = "test04";
    private static final Mpa MPA01 = new Mpa(1, "test01");
    private static final Mpa MPA02 = new Mpa(2, "test02");

    @Test
    void create() {
        Film film = filmRepository.create(getFilm());
        Optional<Film> optionalFilm = filmRepository.findById(film.getId());
        assertThat(optionalFilm)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    void findById() {
        Optional<Film> optionalFilm = filmRepository.findById(TEST_FILM01_ID);
        Film expectedFilm = getFilm();
        expectedFilm.setId(TEST_FILM01_ID);
        Genre genre01 = new Genre(1);
        Genre genre02 = new Genre(2);
        genre01.setName("test01");
        genre02.setName("test02");
        expectedFilm.setGenres(Set.of(genre01, genre02));
        assertThat(optionalFilm)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedFilm);
        Optional<Film> filmNotExist = filmRepository.findById(999);
        assertThat(filmNotExist)
                .isEmpty();
    }

    @Test
    void update() {
        Film expectedFilm = new Film();
        expectedFilm.setId(TEST_FILM01_ID);
        expectedFilm.setName("update");
        expectedFilm.setDescription("update");
        expectedFilm.setDuration(150);
        expectedFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        expectedFilm.setMpa(MPA02);
        filmRepository.update(expectedFilm);
        Optional<Film> optionalFilm = filmRepository.findById(TEST_FILM01_ID);
        assertThat(optionalFilm)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedFilm);
    }

    @Test
    void findAll() {
        List<Film> actualFilms = filmRepository.findAll();
        assertThat(actualFilms)
                .isNotEmpty()
                .containsExactlyElementsOf(getFilms());
    }

    @Test
    void findTopPopularFilms() {
        List<Film> topPopularFilms = filmRepository.findTopPopularFilms(3);
        assertThat(topPopularFilms)
                .isNotEmpty()
                .containsExactlyElementsOf(getTopFilm());
    }

    private static Film getFilm() {
        Film film = new Film();
        film.setName(TEST_FILM01_NAME);
        film.setMpa(MPA01);
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(2020, 5, 10));
        film.setDescription("test01");
        return film;
    }

    private static List<Film> getFilms() {
        Film film01 = new Film();
        film01.setId(TEST_FILM01_ID);
        film01.setName(TEST_FILM01_NAME);
        film01.setMpa(MPA01);
        film01.setDuration(100);
        film01.setReleaseDate(LocalDate.of(2020, 5, 10));
        film01.setDescription("test01");

        Film film02 = new Film();
        film02.setId(TEST_FILM02_ID);
        film02.setName(TEST_FILM02_NAME);
        film02.setMpa(MPA02);
        film02.setDuration(100);
        film02.setReleaseDate(LocalDate.of(2020, 5, 10));
        film02.setDescription("test02");

        Film film03 = new Film();
        film03.setId(TEST_FILM03_ID);
        film03.setName(TEST_FILM03_NAME);
        film03.setMpa(MPA01);
        film03.setDuration(100);
        film03.setReleaseDate(LocalDate.of(2020, 5, 10));
        film03.setDescription("test03");

        Film film04 = new Film();
        film04.setId(TEST_FILM04_ID);
        film04.setName(TEST_FILM04_NAME);
        film04.setMpa(MPA01);
        film04.setDuration(100);
        film04.setReleaseDate(LocalDate.of(2020, 5, 10));
        film04.setDescription("test04");

        return List.of(film01, film02, film03, film04);
    }

    private static List<Film> getTopFilm() {
        Film film01 = new Film();
        film01.setId(TEST_FILM01_ID);
        film01.setName(TEST_FILM01_NAME);
        film01.setMpa(MPA01);
        film01.setDuration(100);
        film01.setReleaseDate(LocalDate.of(2020, 5, 10));
        film01.setDescription("test01");

        Film film02 = new Film();
        film02.setId(TEST_FILM02_ID);
        film02.setName(TEST_FILM02_NAME);
        film02.setMpa(MPA02);
        film02.setDuration(100);
        film02.setReleaseDate(LocalDate.of(2020, 5, 10));
        film02.setDescription("test02");

        Film film03 = new Film();
        film03.setId(TEST_FILM03_ID);
        film03.setName(TEST_FILM03_NAME);
        film03.setMpa(MPA01);
        film03.setDuration(100);
        film03.setReleaseDate(LocalDate.of(2020, 5, 10));
        film03.setDescription("test03");

        return List.of(film01, film02, film03);
    }
}