package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({JdbcFriendShipRepository.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFriendShipRepositoryTest {
    private final JdbcFriendShipRepository friendShipRepository;
    private static final int USER1_ID = 1;
    private static final int USER2_ID = 2;
    private static final int USER3_ID = 3;

    @Test
    void addFriendship() {
        friendShipRepository.addFriend(USER1_ID, USER2_ID);
        List<User> usersExpected = createUsers();
        List<User> friends = friendShipRepository.getFriends(USER1_ID);
        assertEquals(usersExpected, friends);
    }

    @Test
    void removeFriendship() {
        friendShipRepository.removeFriend(USER1_ID, USER3_ID);
        List<User> friends = friendShipRepository.getFriends(USER1_ID);
        assertThat(friends).isEmpty();
    }

    @Test
    void getCommonFriends() {
        List<User> commonFriendsActual = friendShipRepository.getCommonFriends(USER1_ID, USER2_ID);
        List<User> commonFriendsExpected = List.of(getUser());
        assertThat(commonFriendsActual)
                .isNotEmpty()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(commonFriendsExpected);
    }

    static User getUser() {
        User user = new User();
        user.setId(USER3_ID);
        user.setEmail("test3@test.com");
        user.setLogin("testLogin03");
        user.setName("testName03");
        return user;
    }

    static List<User> createUsers() {
        List<User> users = new ArrayList<>();
        User user2 = new User();
        user2.setId(USER2_ID);
        user2.setEmail("test2@test.com");
        user2.setLogin("testLogin02");
        user2.setName("testName02");
        user2.setBirthday(LocalDate.of(1992, 05, 31));
        users.add(user2);
        User user3 = new User();
        user3.setId(USER3_ID);
        user3.setEmail("test3@test.com");
        user3.setLogin("testLogin03");
        user3.setName("testName03");
        user3.setBirthday(LocalDate.of(1992, 05, 31));
        users.add(user3);
        return users;
    }
}