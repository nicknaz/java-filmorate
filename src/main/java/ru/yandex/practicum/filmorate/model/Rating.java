package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import ru.yandex.practicum.filmorate.dao.RatingDbStorage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Rating {
    @NotNull
    int id;
    @NotBlank
    String name;
    RatingDbStorage ratingDbStorage;

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }


    public Rating(int id) {
        this.id = id;
        RatingDbStorage ratingDbStorage = (RatingDbStorage)applicationContext.getBean(RatingDbStorage.class);
        this.name = ratingDbStorage.getRatingName(id);
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
