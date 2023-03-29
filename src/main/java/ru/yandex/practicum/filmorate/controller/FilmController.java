package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmService filmService;
    private UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping
    public Set<Film> getAllFilms() {
        return filmService.getAllfilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        Film film = filmService.getFilmById(id);
        if(film == null){
            throw new NotFoundedException("Фильм не найден");
        }
        return film;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        Film newFilm = filmService.addFilm(film);
        log.info("Добавлен фильм: " + newFilm.toString());
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updFilm = filmService.updateFilm(film);
        log.info("Обновлен фильм: " + updFilm.toString());
        return updFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film unlikeFilm(@PathVariable int id, @PathVariable int userId) {
        if(userService.getUserById(userId) == null){
            throw new NotFoundedException("Пользователь не найден");
        }
        if(filmService.getFilmById(userId) == null){
            throw new NotFoundedException("Фильм не найден");
        }
        return filmService.unlikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }
}
