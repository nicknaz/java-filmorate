package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
public class Genre {
    @NotNull
    int id;
    String name;

    public Genre(int id) {
        this.id = id;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
    /*COMEDY,
    HORROR,
    DOCUMENTARY,
    MUSICAL,
    CARTOONS,
    WESTERN,
    THRILLER,
    ACTION_FILM,
    WAR_FILM,
    HISTORICAL_FILM,
    ROMANTIC_COMEDY,
    ADVENTURE,
    BIOGRAPHICAL,
    DETECTIVE_FILM*/
}
