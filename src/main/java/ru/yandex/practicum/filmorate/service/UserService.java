package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        initFriends(user);
        return user;
    }

    public Set<User> getAllUsers() {
        Set<User> users = userStorage.getUsersSet();
        initFriends(users);
        return users;
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
        friendsDbStorage.makeFriends(userId, friendId);
        User user = getUserById(userId);
        initFriends(user);
        return user;
    }

    public User deleteFriends(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        friendsDbStorage.removeFriend(userId, friendId);
        User user = getUserById(userId);
        initFriends(user);
        return user;
    }

    public List<User> getFriends(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        return getAllUsers().stream()
                .filter(u -> getUserById(id).getFriends().containsKey(u.getId()))
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public Set<User> getCommonFriends(int userId, int otherUserId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(otherUserId) == null) {
            throw new NotFoundedException("Пользователь не найден");
        }
        return getUserById(userId).getFriends().keySet().stream()
                .filter(u -> getUserById(otherUserId).getFriends().containsKey(u))
                .map(u -> getUserById(u))
                .collect(Collectors.toSet());
    }

    private void initFriends(Set<User> users) {
        for (User user: users) {
            initFriends(user);
        }
    }

    private void initFriends(User user) {
        SqlRowSet rowSet = friendsDbStorage.getAllFriends(user.getId());
        while (rowSet.next()) {
            if (rowSet.getInt("userID") == user.getId()) {
                user.getFriends().put(rowSet.getInt("friendId"), rowSet.getBoolean("status"));
            } else if (rowSet.getBoolean("status")) {
                user.getFriends().put(rowSet.getInt("userId"), rowSet.getBoolean("status"));
            }
        }
    }
}
