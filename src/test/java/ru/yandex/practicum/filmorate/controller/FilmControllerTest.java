package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.matcher.ResponseBodyMatcher.responseBody;
@WebMvcTest(FilmController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String URL = "/films";

    @Test
    void whenValidInput_thenReturns200AndResult() throws Exception {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setName("name");
        film.setDescription("desc");
        film.setDuration(100);
        String body = objectMapper.writeValueAsString(film);
        film.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(film, Film.class));
        List<Film> expectedList = List.of(film);
        mockMvc.perform(get(URL)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(expectedList, new TypeReference<List<Film>>() {
                }));
        film.setName("updateName");
        body = objectMapper.writeValueAsString(film);
        mockMvc.perform(put(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(film, Film.class));
    }

    @Test
    void whenNameNull_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDescription("desc");
        film.setDuration(100);
        String body = objectMapper.writeValueAsString(film);
        film.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("name",
                        "The field should not be null"));
    }

    @Test
    void whenDescriptionMore200_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setName("name");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
                "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                "который за время «своего отсутствия», стал кандидатом Коломбани.");
        film.setDuration(100);
        String body = objectMapper.writeValueAsString(film);
        film.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("description",
                        "The description should be no more than 200 characters long"));
    }

    @Test
    void whenReleaseDateIncorrect_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setName("name");
        film.setDescription("desc");
        film.setDuration(100);
        String body = objectMapper.writeValueAsString(film);
        film.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("releaseDate",
                        "The release date is not earlier than December 28, 1895"));
        film.setReleaseDate(LocalDate.of(1894, 12, 27));
        body = objectMapper.writeValueAsString(film);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("releaseDate",
                        "The release date is not earlier than December 28, 1895"));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        body = objectMapper.writeValueAsString(film);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void whenDurationNegative_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setName("name");
        film.setDescription("desc");
        film.setDuration(-100);
        String body = objectMapper.writeValueAsString(film);
        film.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("duration",
                        "The field must be positive"));
    }

    @Test
    void whenIdIncorrect_thenReturns400() throws Exception {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setName("name");
        film.setDescription("desc");
        film.setDuration(100);
        film.setId(1);
        String body = objectMapper.writeValueAsString(film);
        mockMvc.perform(put(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenRequestEmpty_thenReturns400() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());


    }
}