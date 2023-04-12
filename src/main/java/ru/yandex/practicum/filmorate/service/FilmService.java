package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private LikeDbStorage likeDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeDbStorage likeDbStorage) {
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Set<Film> getAllfilms() {
        return filmStorage.getFilmsSet();
    }

    public Film updateFilm(Film film) {
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new NotFoundedException("Фильм не найден");
        }
        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(int filmId, int userId) {
        filmStorage.getFilmById(filmId).getLikes().add(userId);
        likeDbStorage.likeFilm(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film unlikeFilm(int filmId, int userId) {
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
        likeDbStorage.unlikeFilm(filmId, userId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getFilmsSet().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getPopularFilms() {
        return getPopularFilms(10);
    }
}
