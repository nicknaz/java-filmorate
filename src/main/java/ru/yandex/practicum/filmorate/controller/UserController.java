package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Set<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id){
        User user = userService.getUserById(id);
        if(user == null){
            throw new NotFoundedException("Пользователь не найден");
        }
        return user;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user){
        User newUser = userService.addUser(user);
        log.info("Добавлен пользователь: " + newUser.toString());
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user){
        User updUser = userService.updateUser(user);
        log.info("Обновлен пользователь: " + updUser.toString());
        return updUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User makeFriends(@PathVariable int id, @PathVariable int friendId){
        return userService.makeFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable int id, @PathVariable int friendId){
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable int id){
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId){
        return userService.getCommonFriends(id, otherId);
    }
}
