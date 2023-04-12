package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Rating {
    @NotNull
    int id;
    String name;

    public Rating(int id) {
        this.id = id;
    }

    public Rating(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /*G,
    PG,
    PG_13,
    R,
    NC_17*/
}
