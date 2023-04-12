package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
public class GenreService {
    private GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre getGenreById(int id){
        Genre genre = genreDbStorage.getGenreById(id);
        if (genre == null) {
            throw new NotFoundedException("Жанр с таким id не найден!");
        }
        return genre;
    }

    public Collection<Genre> getAllGenres(){
        return genreDbStorage.getGenres();
    }
}
