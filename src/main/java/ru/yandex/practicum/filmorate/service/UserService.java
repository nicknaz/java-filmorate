package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user){
        return userStorage.addUser(user);
    }

    public User getUserById(int id){
        return userStorage.getUserById(id);
    }

    public Set<User> getAllUsers(){
        return userStorage.getUsersSet();
    }

    public User updateUser(User user){
        return userStorage.updateUser(user);
    }

    public User makeFriends(int userId, int friendId){
        try {
            if(userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null){
                throw new NotFoundedException("Пользователь не найден");
            }
            userStorage.getUserById(userId).getFriends().add(friendId);
            userStorage.getUserById(friendId).getFriends().add(userId);
            return userStorage.getUserById(userId);
        }catch (NullPointerException npe){
            throw new NotFoundedException("Пользователь не найден");
        }

    }

    public User deleteFriends(int userId, int friendId){
        try {
            userStorage.getUserById(userId).getFriends().remove(friendId);
            userStorage.getUserById(friendId).getFriends().remove(userId);
            return userStorage.getUserById(userId);
        }catch (NullPointerException npe){
            throw new NotFoundedException("Пользователь не найден");
        }
    }

    public List<User> getFriends(int id){
        try {
            return userStorage.getUsersSet().stream()
                    .filter(u -> userStorage.getUserById(id).getFriends().contains(u.getId()))
                    .sorted(new Comparator<User>() { //только для тестов
                        @Override
                        public int compare(User o1, User o2) {
                            return o1.getId() - o2.getId();
                        }
                    })
                    .collect(Collectors.toList());
        }catch (NullPointerException npe){
            throw new NotFoundedException("Пользователь не найден");
        }
    }

    public Set<User> getCommonFriends(int userId, int otherUserId){
        try {
            return userStorage.getUserById(userId).getFriends().stream()
                    .filter(u -> userStorage.getUserById(otherUserId).getFriends().contains(u))
                    .map(u -> userStorage.getUserById(u))
                    .collect(Collectors.toSet());
        }catch (NullPointerException npe){
            throw new NotFoundedException("Пользователь не найден");
        }
    }
}