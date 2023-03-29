package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage{

    private Map<Integer, User> users;
    int generatorId = 1;

    private InMemoryUserStorage() {
        users = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        user.setId(generatorId);
        users.put(generatorId, user);
        generatorId++;
        return users.get(generatorId-1);
    }

    @Override
    public User updateUser(User user) {
        if(users.containsKey(user.getId())){
            users.put(user.getId(), user);
        }
        return users.get(user.getId());
    }

    @Override
    public Set<User> getUsersSet() {
        return new HashSet<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public void removeAllUsers() {
        users.clear();
    }

    @Override
    public void removeUser(int id) {
        users.remove(id);
    }
}
