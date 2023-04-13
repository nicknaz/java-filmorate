package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.FilmDatePositive;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
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
    private Set<Integer> likes;
    private Collection<Genre> genres;
    private Integer rate = 0;
    @NotNull
    private Rating mpa;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
                Set<Integer> likes, Collection<Genre> genres, Integer rate, Rating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        if (likes == null) {
            this.likes = new HashSet<>();
        } else {
            this.likes = likes;
        }
        if (genres == null) {
            this.genres = new HashSet<>();
        } else {
            this.genres = genres;
        }
        if (rate != null) {
            this.rate = rate;
        }
        this.mpa = mpa;
    }

}
