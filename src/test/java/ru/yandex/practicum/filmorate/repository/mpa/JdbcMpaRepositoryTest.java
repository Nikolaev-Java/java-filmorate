package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcMpaRepository.class, MpaRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcMpaRepositoryTest {
    private final JdbcMpaRepository mpaRepository;
    private final static int TEST_MPA01_ID = 1;
    private final static int TEST_MPA02_ID = 2;

    @Test
    void findAll() {
        List<Mpa> result = mpaRepository.findAll();
        List<Mpa> expected = getMpas();
        assertThat(result)
                .isNotEmpty()
                .containsExactlyElementsOf(expected);
    }

    @Test
    void findById() {
        Mpa expected = new Mpa(1, "test01");
        Optional<Mpa> mpaOptional = mpaRepository.findById(TEST_MPA01_ID);
        assertThat(mpaOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expected);
        Optional<Mpa> mpaNotExist = mpaRepository.findById(999);
        assertThat(mpaNotExist)
                .isEmpty();
    }

    private static List<Mpa> getMpas() {
        return List.of(new Mpa(1, "test01"), new Mpa(2, "test02"));
    }
}