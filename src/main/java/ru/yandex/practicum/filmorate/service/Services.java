package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface Services<T> {
    T create(T model);

    T findById(int id);

    Collection<T> getAll();

    T update(T model);
}
