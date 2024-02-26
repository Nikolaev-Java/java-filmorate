package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements Services<User> {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User create(User model) {
        if (model.getName() == null || model.getName().isBlank()) {
            model.setName(model.getLogin());
        }
        userStorage.create(model);
        log.debug("Create user - {}", model);
        return model;
    }

    @Override
    public User findById(int id) {
        return userStorage.read(id);
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.findAll();
    }

    @Override
    public User update(User user) {
        userStorage.update(user);
        log.debug("Update user - {}", user);
        return user;
    }

    public void addFriend(int userId, int friendsId) {
        User friend = userStorage.read(friendsId);
        User user = userStorage.read(userId);
        user.addFriend(friendsId);
        friend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendsId) {
        userStorage.read(friendsId);
        userStorage.read(userId).deleteFriend(friendsId);
    }

    public List<User> findAllFriendsUser(int id) {
        User user = userStorage.read(id);
        return user.getFriends().stream()
                .map(userStorage::read)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriendsThisAnotherUser(int id, int otherId) {
        User user = userStorage.read(id);
        User otherUser = userStorage.read(otherId);
        return user.getFriends().stream()
                .filter(i -> otherUser.getFriends().contains(i))
                .map(userStorage::read)
                .collect(Collectors.toList());
    }
}
