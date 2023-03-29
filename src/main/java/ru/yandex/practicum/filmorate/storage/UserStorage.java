package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    Set<User> getUsersSet();
    User getUserById(int id);
    void removeAllUsers();
    void removeUser(int id);
}
