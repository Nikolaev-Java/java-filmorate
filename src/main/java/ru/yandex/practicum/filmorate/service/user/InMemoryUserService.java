package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public InMemoryUserService(UserStorage userStorage) {
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
        return userStorage.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User update(User user) {
        userStorage.update(user);
        log.debug("Update user - {}", user);
        return user;
    }

    public void addFriend(int userId, int friendsId) {
        User friend = userStorage.findById(friendsId);
        User user = userStorage.findById(userId);
        user.addFriend(friendsId);
        friend.addFriend(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendsId) {
        userStorage.contains(friendsId);
        userStorage.findById(userId).deleteFriend(friendsId);
    }

    public List<User> findAllFriendsUser(int id) {
        User user = userStorage.findById(id);
        return user.getFriends().stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriendsThisAnotherUser(int id, int otherId) {
        User user = userStorage.findById(id);
        User otherUser = userStorage.findById(otherId);
        return user.getFriends().stream()
                .filter(i -> otherUser.getFriends().contains(i))
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }
}
