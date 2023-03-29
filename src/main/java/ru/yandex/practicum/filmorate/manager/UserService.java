package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserService {
    private static UserService instance;
    private static Map<Integer,User> users;
    static int generatorId = 1;

    private UserService(){
        users = new HashMap<>();
    }

    public static UserService getInstance(){
        if(instance == null)
            instance = new UserService();
        return instance;
    }

    public User addUser(User user){
        user.setId(generatorId);
        users.put(generatorId, user);
        generatorId++;
        return users.get(generatorId-1);
    }

    public User getUserById(int id){
        return users.get(id);
    }

    public Set<User> getAllUsers(){
        return new HashSet<>(users.values());
    }

    public User updateUser(User user){
        if(users.containsKey(user.getId())){
            users.put(user.getId(), user);
        }
        return users.get(user.getId());
    }
}
