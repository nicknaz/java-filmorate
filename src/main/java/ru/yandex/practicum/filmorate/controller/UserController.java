package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> getAllFilms(){
        return users;
    }

    @PostMapping
    public User addFilm(@RequestBody User user){

        users.add(user);
        log.info("Добавлен пользователь: " + user.toString());
        return user;
    }

    @PutMapping
    public User updateFilm(@RequestBody User user){
        users.add(user);
        log.info("Обновлен пользователь: " + user.toString());
        return user;
    }
}
