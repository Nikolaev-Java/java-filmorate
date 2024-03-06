package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public void create(User user) {
        calcId();
        user.setId(id);
        users.put(id, user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void update(User user) {
        contains(user.getId());
        users.put(user.getId(), user);
    }

    @Override
    public User findById(int id) {
        contains(id);
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void contains(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("The user with the id " + id + " was not found", "User");
        }
    }

    private void calcId() {
        this.id++;
    }
}
