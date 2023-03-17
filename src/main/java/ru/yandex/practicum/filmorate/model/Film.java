package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.FilmDatePositive;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotEmpty
    @NotBlank
    private String name;
    @NotEmpty
    @Size(max = 200)
    private String description;
    @FilmDatePositive
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
}
