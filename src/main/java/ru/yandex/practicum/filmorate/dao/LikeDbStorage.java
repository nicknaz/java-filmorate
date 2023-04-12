package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Integer> getFilmLikes(int filmId){
        String sqlQuery = "SELECT userId FROM likes WHERE filmId = ?";
        return new HashSet<Integer>(jdbcTemplate.query(sqlQuery,
                (ResultSet resultSet, int rowNum) -> resultSet.getInt("userId"),
                filmId));
    }

    public void likeFilm(int filmId, int userId){
        String sqlQuery = "INSERT INTO likes(filmId, userId) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void unlikeFilm(int filmId, int userId){
        String sqlQuery = "DELETE FROM likes WHERE filmId = ? AND userId = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}
