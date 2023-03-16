package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmsManager;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmsManager filmsManager = FilmsManager.getInstance();

    @GetMapping
    public Set<Film> getAllFilms(){
        return filmsManager.getAllfilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        Film newFilm = filmsManager.addFilm(film);
        log.info("Добавлен фильм: " + newFilm.toString());
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        Film updFilm = filmsManager.updateFilm(film);
        log.info("Обновлен фильм: " + updFilm.toString());
        return updFilm;
    }
}
