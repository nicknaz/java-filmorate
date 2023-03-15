package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.DurationPositive;
import ru.yandex.practicum.filmorate.annotation.FilmDatePositive;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

import static java.time.Duration.*;

@Data
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @FilmDatePositive
    private LocalDate releaseDate;
    @DurationPositive
    private Duration duration;
}
