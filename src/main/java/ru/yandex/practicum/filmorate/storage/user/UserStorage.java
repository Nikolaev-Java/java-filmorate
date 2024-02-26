package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void create(User user);

    User read(int id);

    void update(User user);

    void delete(User user);

    Collection<User> findAll();
}

