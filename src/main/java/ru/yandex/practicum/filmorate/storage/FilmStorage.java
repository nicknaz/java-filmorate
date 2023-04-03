package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Set<Film> getFilmsSet();

    Film getFilmById(int id);

    void removeAllFilms();

    void removeFilm(int id);
}
