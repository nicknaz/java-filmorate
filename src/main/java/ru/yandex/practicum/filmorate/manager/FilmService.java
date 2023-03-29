package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FilmService {
    private static FilmService instance;
    private static Map<Integer, Film> films;
    static int generatorId = 1;

    private FilmService(){
        films = new HashMap<>();
    }

    public static FilmService getInstance(){
        if(instance == null)
            instance = new FilmService();
        return instance;
    }

    public Film addFilm(Film film){
        film.setId(generatorId);
        films.put(generatorId, film);
        generatorId++;
        return films.get(generatorId-1);
    }

    public Film getFilmById(int id){
        return films.get(id);
    }

    public Set<Film> getAllfilms(){
        return new HashSet<>(films.values());
    }

    public Film updateFilm(Film film){
        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }
}
