package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.repository.user.FriendShipRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendShipRepository friendShipRepository;


    @Override
    public User create(User model) {
        if (model.getName() == null || model.getName().isBlank()) {
            model.setName(model.getLogin());
        }
        userRepository.create(model);
        log.debug("Create user - {}", model);
        return model;
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The user with the id " + id + " was not found", "User"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        userRepository.update(user);
        log.debug("Update user - {}", user);
        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("The user with the id " + friendId + " was not found", "User"));
        friendShipRepository.addFriend(userId, friendId);
    }

    @Override
    public List<User> findAllFriendsUser(int id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The user with the id " + id + " was not found", "User"));
        return friendShipRepository.getFriends(id);
    }

    @Override
    public List<User> findCommonFriendsThisAnotherUser(int id, int otherId) {
        return friendShipRepository.getCommonFriends(id, otherId);
    }

    @Override
    public void deleteFriend(int userId, int friendsId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("The user with the id " + userId + " was not found", "User"));
        userRepository.findById(friendsId)
                .orElseThrow(() -> new NotFoundException("The user with the id " + friendsId + " was not found", "User"));
        friendShipRepository.removeFriend(userId, friendsId);
    }


}
