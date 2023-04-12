package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RatingDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getRatingName(int id) {
        String sqlQuery = "SELECT name FROM ratings WHERE ratingId = ?";
        return jdbcTemplate.query(sqlQuery,
                (ResultSet resultSet, int rowNum) -> resultSet.getString("name"),
                id).get(0);
    }

    public Rating getRatingById(int id) {
        String sqlQuery = "SELECT * FROM ratings WHERE ratingId = ?";
        List<Rating> ratings = jdbcTemplate.query(sqlQuery, this::mapRowToRating, id);
        Rating rating = ratings.size() > 0 ? ratings.get(0) : null;
        return rating;
    }

    public Set<Rating> getRatings() {
        String sqlQuery = "SELECT * FROM ratings";
        return new HashSet<>(jdbcTemplate.query(sqlQuery,
                this::mapRowToRating));
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt("ratingId"), resultSet.getString("name"));
    }


}
