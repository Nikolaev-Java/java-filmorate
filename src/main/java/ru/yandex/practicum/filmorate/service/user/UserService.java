package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User findById(int id);

    List<User> findAll();

    User update(User user);

    void addFriend(int userId, int friendId);

    List<User> findAllFriendsUser(int id);

    List<User> findCommonFriendsThisAnotherUser(int id, int otherId);

    void deleteFriend(int userId, int friendsId);
}
