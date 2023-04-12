package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Set;

@Service
public class MpaService {
    private RatingDbStorage ratingDbStorage;

    @Autowired
    public MpaService(RatingDbStorage ratingDbStorage) {
        this.ratingDbStorage = ratingDbStorage;
    }

    public Rating getMpaById(int id){
        Rating rating = ratingDbStorage.getRatingById(id);
        if (rating == null) {
            throw new NotFoundedException("Рейтинг с таким id не найден!");
        }
        return rating;
    }

    public Set<Rating> getAllMpa(){
        return ratingDbStorage.getRatings();
    }
}
