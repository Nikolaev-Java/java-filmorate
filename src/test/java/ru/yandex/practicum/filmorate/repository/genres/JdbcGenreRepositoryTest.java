package ru.yandex.practicum.filmorate.repository.genres;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcGenreRepository.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcGenreRepositoryTest {
    private final JdbcGenreRepository genreRepository;
    private static final int TEST_GENRE01_ID = 1;
    private static final int TEST_GENRE02_ID = 2;
    private static final int TEST_GENRE03_ID = 3;

    @Test
    void findAll() {
        List<Genre> genres = genreRepository.findAll();
        List<Genre> expectedGenres = getTestGenres();
        assertThat(genres)
                .isNotEmpty()
                .containsExactlyElementsOf(expectedGenres);
    }

    @Test
    void findById() {
        Optional<Genre> optionalGenre = genreRepository.findById(TEST_GENRE01_ID);
        Genre genre = new Genre(TEST_GENRE01_ID);
        assertThat(optionalGenre)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(genre);
        Optional<Genre> optionalGenreNotExist = genreRepository.findById(TEST_GENRE03_ID);
        assertThat(optionalGenreNotExist)
                .isEmpty();
    }

    @Test
    void findByIds() {
        genreRepository.findByIds(List.of(TEST_GENRE01_ID, TEST_GENRE02_ID));
        List<Genre> expectedGenres = getTestGenres();
        assertThat(expectedGenres)
                .isNotEmpty()
                .containsExactlyElementsOf(expectedGenres);
    }

    private static List<Genre> getTestGenres() {
        Genre genre01 = new Genre(TEST_GENRE01_ID);
        genre01.setName("test01");
        Genre genre02 = new Genre(TEST_GENRE02_ID);
        genre02.setName("test02");
        return List.of(genre01, genre02);
    }
}