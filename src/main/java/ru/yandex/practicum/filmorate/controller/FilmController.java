package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmService filmService = FilmService.getInstance();

    @GetMapping
    public Set<Film> getAllFilms(){
        return filmService.getAllfilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        Film newFilm = filmService.addFilm(film);
        log.info("Добавлен фильм: " + newFilm.toString());
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        Film updFilm = filmService.updateFilm(film);
        log.info("Обновлен фильм: " + updFilm.toString());
        return updFilm;
    }
}
