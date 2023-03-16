package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.UsersManager;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UsersManager usersManager = UsersManager.getInstance();

    @GetMapping
    public Set<User> getAllFilms(){
        return usersManager.getAllUsers();
    }

    @PostMapping
    public User addFilm(@Valid @RequestBody User user){
        User newUser = usersManager.addUser(user);
        log.info("Добавлен пользователь: " + newUser.toString());
        return newUser;
    }

    @PutMapping
    public User updateFilm(@Valid @RequestBody User user){
        User updUser = usersManager.updateUser(user);
        log.info("Обновлен пользователь: " + updUser.toString());
        return updUser;
    }
}
