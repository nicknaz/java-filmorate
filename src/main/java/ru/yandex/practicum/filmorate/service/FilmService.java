package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.dao.FilmRatingDbStorage;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private FilmGenreDbStorage filmGenreDbStorage;
    private FilmRatingDbStorage filmRatingDbStorage;
    private LikeDbStorage likeDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeDbStorage likeDbStorage,
                       FilmGenreDbStorage filmGenreDbStorage, FilmRatingDbStorage filmRatingDbStorage) {
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
        this.filmGenreDbStorage = filmGenreDbStorage;
        this.filmRatingDbStorage = filmRatingDbStorage;
    }

    public Film addFilm(Film film) {
        Film filmAdded = filmStorage.addFilm(film);
        filmGenreDbStorage.updateFilmGenres(film.getId(), new HashSet<>(film.getGenres()));
        initLikesAndGenres(filmAdded);
        return filmAdded;
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundedException("Фильм не найден");
        }
        initLikesAndGenres(film);
        return film;
    }

    public Set<Film> getAllfilms() {
        Set<Film> films = filmStorage.getFilmsSet();
        initLikesAndGenres(films);
        return films;
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new NotFoundedException("Фильм не найден");
        }
        filmStorage.updateFilm(film);
        filmGenreDbStorage.updateFilmGenres(film.getId(), new HashSet<>(film.getGenres()));
        Film filmUpdated = filmStorage.getFilmById(film.getId());
        initLikesAndGenres(filmUpdated);
        return filmUpdated;
    }

    public Film likeFilm(int filmId, int userId) {
        filmStorage.getFilmById(filmId).getLikes().add(userId);
        likeDbStorage.likeFilm(filmId, userId);
        Film film = filmStorage.getFilmById(filmId);
        initLikesAndGenres(film);
        return film;
    }

    public Film unlikeFilm(int filmId, int userId) {
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
        likeDbStorage.unlikeFilm(filmId, userId);
        Film film = filmStorage.getFilmById(filmId);
        initLikesAndGenres(film);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        Set<Film> films = filmStorage.getFilmsSet();
        initLikesAndGenres(films);
        return films.stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getPopularFilms() {
        return getPopularFilms(10);
    }

    private void initLikesAndGenres(Set<Film> films) {
        for (Film film : films) {
            initLikesAndGenres(film);
        }
    }

    private void initLikesAndGenres(Film film) {
        film.setGenres(filmGenreDbStorage.getFilmGenres(film.getId()));
        film.setLikes(likeDbStorage.getFilmLikes(film.getId()));
    }
}
