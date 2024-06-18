package ru.yandex.practicum.filmorate.repository.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcUserRepository.class, UserRowMapper.class})
class JdbcUserRepositoryTest {
    private final JdbcUserRepository userRepository;
    private static final int TEST_USER01_ID = 1;
    private static final int TEST_USER02_ID = 2;
    private static final int TEST_USER03_ID = 3;
    private static final int TEST_NOT_EXIST_ID = 999;

    @Autowired
    JdbcUserRepositoryTest(JdbcUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void findUserById() {
        Optional<User> userOptional = userRepository.findById(TEST_USER01_ID);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getUser());
        Optional<User> userOptionalNotExist = userRepository.findById(TEST_NOT_EXIST_ID);
        assertThat(userOptionalNotExist)
                .isEmpty();
    }

    @Test
    void findAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users)
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(createUsers());
    }

    @Test
    void createUser() {
        User testUser = new User();
        testUser.setName("create1");
        testUser.setEmail("create1@gmail.com");
        testUser.setLogin("create1");
        testUser.setBirthday(LocalDate.of(1992, 5, 3));
        User actualUser = userRepository.create(testUser);
        Optional<User> userOptional = userRepository.findById(actualUser.getId());
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(testUser);
    }

    @Test
    void updateUser() {
        User testUser = new User();
        testUser.setId(TEST_USER01_ID);
        testUser.setName("update1");
        testUser.setEmail("update1@gmail.com");
        testUser.setLogin("update1");
        testUser.setBirthday(LocalDate.of(1992, 5, 3));
        userRepository.update(testUser);
        Optional<User> userOptional = userRepository.findById(testUser.getId());
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(testUser);
    }

    static User getUser() {
        User user = new User();
        user.setId(TEST_USER01_ID);
        user.setEmail("test1@test.com");
        user.setLogin("testLogin01");
        user.setName("testName01");
        user.setBirthday(LocalDate.of(1992, 05, 31));
        return user;
    }

    static List<User> createUsers() {
        List<User> users = new ArrayList<>();
        users.add(getUser());
        User user2 = new User();
        user2.setId(TEST_USER02_ID);
        user2.setEmail("test2@test.com");
        user2.setLogin("testLogin02");
        user2.setName("testName02");
        user2.setBirthday(LocalDate.of(1992, 05, 31));
        users.add(user2);
        User user3 = new User();
        user3.setId(TEST_USER03_ID);
        user3.setEmail("test3@test.com");
        user3.setLogin("testLogin03");
        user3.setName("testName03");
        user3.setBirthday(LocalDate.of(1992, 05, 31));
        users.add(user3);
        return users;
    }
}