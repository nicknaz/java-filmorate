package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void makeFriends(int userId, int friendId) {
        String sqlQuery = "SELECT * FROM friends WHERE (userId = ? OR friendID = ?) AND (userId = ? OR friendID = ?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId, userId, friendId, friendId);
        if (rowSet.next()) {
            if (rowSet.getInt("friendId") == userId) {
                sqlQuery = "UPDATE friends SET status = true WHERE userId = ? AND friendId = ?";
                jdbcTemplate.update(sqlQuery, friendId, userId);
            }
        } else {
            sqlQuery = "INSERT INTO friends(userId, friendId, status) VALUES (?, ?, false)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } //H2 почему то не работает с upsert
    }

    public SqlRowSet getAllFriends(int userId) {
        String sqlQuery = "SELECT * FROM friends WHERE userId = ? OR friendId = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, userId, userId);
    }

    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM friends WHERE (userId = ? OR friendId = ?) AND (userId = ? OR friendId = ?)";
        jdbcTemplate.update(sqlQuery, userId, userId, friendId, friendId);
    }
}
