package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    FriendsDbStorage friendsDbStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate, @Autowired FriendsDbStorage friendsDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsDbStorage = friendsDbStorage;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"userId"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE userId = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        /*for (Integer id : user.getFriends().keySet()) {
            if(user.getFriends().get(id) == FriendStatus.FRIEND){
                friendsDbStorage.makeFriends(user.getId(), id, true);
            }else{
                friendsDbStorage.makeFriends(user.getId(), id, false);
            }
        }*/
        return user;
    }

    @Override
    public Set<User> getUsersSet() {
        String sqlQuery = "SELECT userId, email, login, name, birthday FROM users";
        Set<User> users = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser));
        return users;
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT userId, email, login, name, birthday FROM users WHERE userId = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        User user = users.size() > 0 ? users.get(0) : null;
        return user;
    }

    @Override
    public void removeAllUsers() {
        String sqlQuery = "DELETE FROM users";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public void removeUser(int id) {
        String sqlQuery = "DELETE FROM users WHERE userId = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("userId"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        SqlRowSet rowSet = friendsDbStorage.getAllFriends(user.getId());
        while (rowSet.next()) {
            if (rowSet.getInt("userID") == user.getId()) {
                user.getFriends().put(rowSet.getInt("friendId"), rowSet.getBoolean("status"));
            } else if(rowSet.getBoolean("status")) {
                user.getFriends().put(rowSet.getInt("userId"), rowSet.getBoolean("status"));
            }
        }
        return user;
    }
}
