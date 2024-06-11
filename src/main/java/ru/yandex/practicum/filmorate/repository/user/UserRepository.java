package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    Optional<User> findById(int id);

    void update(User user);

    List<User> findAll();

    boolean contains(int id);
}

