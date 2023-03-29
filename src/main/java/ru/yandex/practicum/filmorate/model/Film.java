package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.FilmDatePositive;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
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

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Set<Integer> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        if(likes == null){
            this.likes = new HashSet<>();
        }else{
            this.likes = likes;
        }
    }

}
