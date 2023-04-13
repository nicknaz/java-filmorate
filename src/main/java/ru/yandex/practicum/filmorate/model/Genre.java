package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Configurable
public class Genre {
    @NotNull
    int id;
    @NotBlank
    String name;
    GenreDbStorage genreDbStorage;

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public Genre(int id) {
        this.id = id;
        GenreDbStorage genreDbStorage  = (GenreDbStorage)applicationContext.getBean(GenreDbStorage.class);
        this.name = genreDbStorage.getGenreName(id);
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return id == genre.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
