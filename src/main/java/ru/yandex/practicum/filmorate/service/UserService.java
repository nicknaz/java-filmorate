package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserStorage userStorage;
    private FriendsDbStorage friendsDbStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsDbStorage friendsDbStorage) {
        this.userStorage = userStorage;
        this.friendsDbStorage = friendsDbStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Set<User> getAllUsers() {
        return userStorage.getUsersSet();
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        return userStorage.updateUser(user);
    }

    public User makeFriends(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        friendsDbStorage.makeFriends(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public User deleteFriends(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        friendsDbStorage.removeFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    public List<User> getFriends(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        return userStorage.getUsersSet().stream()
                .filter(u -> userStorage.getUserById(id).getFriends().containsKey(u.getId()))
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public Set<User> getCommonFriends(int userId, int otherUserId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(otherUserId) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        return userStorage.getUserById(userId).getFriends().keySet().stream()
                .filter(u -> userStorage.getUserById(otherUserId).getFriends().containsKey(u))
                .map(u -> userStorage.getUserById(u))
                .collect(Collectors.toSet());
    }
}
