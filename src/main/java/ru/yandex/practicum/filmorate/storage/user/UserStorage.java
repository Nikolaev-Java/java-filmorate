package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void create(User user);

    User findById(int id);

    void update(User user);

    void delete(User user);

    List<User> findAll();

    boolean contains(int id);
}

