package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film){
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(int id){
        return filmStorage.getFilmById(id);
    }

    public Set<Film> getAllfilms(){
        return filmStorage.getFilmsSet();
    }

    public Film updateFilm(Film film){
        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(int filmId, int userId){
        filmStorage.getFilmById(filmId).getLikes().add(userId);
        return filmStorage.getFilmById(filmId);
    }

    public Film unlikeFilm(int filmId, int userId){
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getPopularFilms(int count){
        return filmStorage.getFilmsSet().stream().sorted(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o2.getLikes().size() - o1.getLikes().size();
            }
        }).limit(count).collect(Collectors.toList());
    }

    public List<Film> getPopularFilms(){
        return getPopularFilms(10);
    }
}
