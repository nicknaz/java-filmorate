package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Rating> getAllMpa() {
        return mpaService.getAllMpa().stream().sorted((o1, o2) -> o1.getId() - o2.getId()).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Rating getMpa(@PathVariable int id) {
        Rating rating = mpaService.getMpaById(id);
        if (rating == null) {
            throw new NotFoundedException("Mpa не найден");
        }
        return rating;
    }
}
