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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.matcher.ResponseBodyMatcher.responseBody;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private UserServiceImpl userServiceImpl;
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
        System.out.println(body);
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
    void whenAddFriendUser_thenReturns200() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@google.com");
        User user1 = new User();
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        String body1 = objectMapper.writeValueAsString(user1);
        System.out.println(body);
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body));
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body1));
        mockMvc.perform(put(URL + "/1/friends/2")
                        .contentType("application/json"))
                .andExpect(status().isOk());
        user.setId(1);
        user.addFriend(2);
        user1.setId(2);
        user1.addFriend(1);
        mockMvc.perform(get(URL + "/1")
                        .contentType("application/json"))
                .andExpect(responseBody().containsObjectAsJson(user, User.class));
        mockMvc.perform(get(URL + "/2")
                        .contentType("application/json"))
                .andExpect(responseBody().containsObjectAsJson(user1, User.class));
        mockMvc.perform(put(URL + "/1/friends/-2")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().containsError("User",
                        "The user with the id -2 was not found"));
    }

    @Test
    void whenGetFriendsUser_thenReturns200AndEqualsResult() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@google.com");
        User user1 = new User();
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setEmail("email@google.com");
        User user2 = new User();
        user2.setBirthday(LocalDate.of(2000, 1, 1));
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        String body1 = objectMapper.writeValueAsString(user1);
        String body2 = objectMapper.writeValueAsString(user2);
        System.out.println(body);
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body));
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body1));
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body2));
        mockMvc.perform(put(URL + "/1/friends/2")
                .contentType("application/json"));
        mockMvc.perform(put(URL + "/1/friends/3")
                .contentType("application/json"));
        mockMvc.perform(put(URL + "/2/friends/3")
                .contentType("application/json"));
        user.setId(1);
        user.addFriend(2);
        user.addFriend(3);
        user1.setId(2);
        user1.addFriend(3);
        user1.addFriend(1);
        user2.setId(3);
        user2.addFriend(1);
        user2.addFriend(2);
        List<User> friends = List.of(user, user2);
        mockMvc.perform(get(URL + "/2/friends")
                        .contentType("application/json"))
                .andExpect(responseBody().containsListAsJson(friends, new TypeReference<List<User>>() {
                }));
        List<User> commonFriends = List.of(user2);
        mockMvc.perform(get(URL + "/1/friends/common/2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(commonFriends, new TypeReference<List<User>>() {
                }));
    }

    @Test
    void whenDeleteFriendUser_thenReturns200() throws Exception {
        User user = new User();
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@google.com");
        User user1 = new User();
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        user1.setLogin("login1");
        user1.setName("name1");
        user1.setEmail("email@google.com");
        String body = objectMapper.writeValueAsString(user);
        String body1 = objectMapper.writeValueAsString(user1);
        System.out.println(body);
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body));
        mockMvc.perform(post(URL)
                .contentType("application/json")
                .content(body1));
        mockMvc.perform(put(URL + "/1/friends/2")
                .contentType("application/json"));
        user.setId(1);
        user1.setId(2);
        mockMvc.perform(delete(URL + "/1/friends/2")
                        .contentType("aplication/json"))
                .andExpect(status().isOk());
        mockMvc.perform(get(URL + "/1")
                        .contentType("aplication/json"))
                .andExpect(responseBody().containsObjectAsJson(user, User.class));
        mockMvc.perform(delete(URL + "/1/friends/3")
                        .contentType("aplication/json"))
                .andExpect(status().isNotFound())
                .andExpect(responseBody().containsError("User",
                        "A friend with this 3 was not found"));
    }
}