package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmRatingDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Rating getRating(int ratingId){
        String sqlQuery = "SELECT * FROM Ratings" +
                " WHERE ratingId = ?";
        List<Rating> ratings = jdbcTemplate.query(sqlQuery, this::mapRowToRating, ratingId);
        Rating rating =  ratings.size() > 0 ? ratings.get(0) : null;
        return rating;
    }

    public int getRatingIdByName(String name){
        String sqlQuery = "SELECT ratingId FROM Ratings" +
                " WHERE name = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, name);
        if (rowSet.next()) {
            Integer ratingId = rowSet.getInt("ratingId");
            return ratingId;
        } else {
            throw new NotFoundedException("Рейтинг не найден");
        }
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt("ratingId"), resultSet.getString("name"));
    }
}
