package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films;
    int generatorId = 1;

    private InMemoryFilmStorage(){
        films = new HashMap<>();
    }


    @Override
    public Film addFilm(Film film) {
        film.setId(generatorId);
        films.put(generatorId, film);
        generatorId++;
        return films.get(generatorId-1);
    }

    @Override
    public Film updateFilm(Film film) {
        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }

    @Override
    public Set<Film> getFilmsSet() {
        return new HashSet<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public void removeAllFilms() {
        films.clear();
    }

    @Override
    public void removeFilm(int id) {
        films.remove(id);
    }
}
