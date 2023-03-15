package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

@RestController
@Slf4j
public class FilmController {

    private Set<Film> films = new HashSet<>();

    @GetMapping("/films")
    public Set<Film> getAllFilms(){
        return films;
    }

    @PostMapping("/film")
    public Film addFilm(@Valid @RequestBody Film film){
        films.add(film);
        log.info("Добавлен фильм: " + film.toString());
        return film;
    }

    @PutMapping("/film")
    public Film updateFilm(@Valid @RequestBody Film film){
        films.add(film);
        log.info("Обновлен фильм: " + film.toString());
        return film;
    }
}
