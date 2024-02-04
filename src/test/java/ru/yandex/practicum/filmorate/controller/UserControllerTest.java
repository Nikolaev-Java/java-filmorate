package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.matcher.ResponseBodyMatcher.responseBody;

@WebMvcTest(controllers = UserController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String URL = "/users";

    @Test
    void whenValidInput_thenReturns200AndResult() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("name");
        user.setLogin("login");
        user.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        user.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(user, User.class));
        List<User> expectedList = List.of(user);
        mockMvc.perform(get(URL)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(expectedList, new TypeReference<List<User>>() {
                }));
        user.setLogin("updateLogin");
        body = objectMapper.writeValueAsString(user);
        mockMvc.perform(put(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(user, User.class));
    }
    @Test
    void whenLoginNull_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("name");
        user.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        user.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("login",
                        "The field should not be null"));
    }
    @Test
    void whenLoginBlank_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("");
        user.setName("name");
        user.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        user.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("login",
                        "The login must not contain spaces or not be empty"));
    }
    @Test
    void whenLoginContainSpaces_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("log in");
        user.setName("name");
        user.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        user.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("login",
                        "The login must not contain spaces or not be empty"));
    }
    @Test
    void whenEmailIncorrect_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("login");
        user.setName("name");
        user.setEmail("emailgoogle.com@");
        String body = objectMapper.writeValueAsString(user);
        user.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("email",
                        "The email is not correct"));
    }
    @Test
    void whenBirthdayIncorrect_thenReturns500AndErrorResult_withFluentApi() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2300, 1, 1));
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        user.setId(1);
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("birthday",
                        "The date of birth should not be in the future"));
    }
    @Test
    void whenNameBlank_thenReturns200() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("login");
        user.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        user.setId(1);
        user.setName("login");
        mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(user, User.class));
    }
    @Test
    void whenIdIncorrect_thenReturns400() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@google.com");
        user.setId(1);
        String body = objectMapper.writeValueAsString(user);
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