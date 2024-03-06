package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.InMemoryFilmService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.matcher.ResponseBodyMatcher.responseBody;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private InMemoryFilmService inMemoryFilmService;
    @Mock
    private UserController userController;
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

    @Test
    void whenAddLike_thenReturns200AndResult() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("name");
        user.setLogin("login");
        user.setEmail("email@google.com");
        String bodyUser = objectMapper.writeValueAsString(user);
        User user2 = new User();
        user2.setBirthday(LocalDate.of(2000, 1, 1));
        user2.setName("name2");
        user2.setLogin("login2");
        user2.setEmail("email@google.com");
        String bodyUser2 = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(bodyUser));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(bodyUser2));
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setName("name");
        film.setDescription("desc");
        film.setDuration(100);
        String body = objectMapper.writeValueAsString(film);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
        mockMvc.perform(put(URL + "/1/like/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());
        mockMvc.perform(put(URL + "/1/like/-2")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().containsError("User",
                        "The user with the id -2 was not found"));
        mockMvc.perform(put(URL + "/-2/like/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().containsError("Film",
                        "The movie with the id -2 was not found"));
        mockMvc.perform(delete(URL + "/1/like/1"))
                .andExpect(status().isOk());
        Film film2 = new Film();
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));
        film2.setName("name");
        film2.setDescription("desc");
        film2.setDuration(100);
        String body2 = objectMapper.writeValueAsString(film2);
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body2));
        mockMvc.perform(put(URL + "/2/like/1"))
                .andExpect(status().isOk());
        mockMvc.perform(put(URL + "/2/like/2"))
                .andExpect(status().isOk());
        film2.addLike(1);
        film2.addLike(2);
        film2.setId(2);
        List<Film> expectedList = List.of(film2);
        System.out.println(film2.getLikes());
        mockMvc.perform(get(URL + "/popular?count=1"))
                .andExpect(responseBody().containsListAsJson(expectedList, new TypeReference<List<Film>>() {
                }));
    }
}