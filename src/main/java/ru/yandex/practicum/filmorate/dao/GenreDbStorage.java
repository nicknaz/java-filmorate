package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getGenreName(int id) {
        String sqlQuery = "SELECT name FROM genres WHERE genreId = ?";
        return jdbcTemplate.query(sqlQuery,
                (ResultSet resultSet, int rowNum) -> resultSet.getString("name"),
                id).get(0);
    }

    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE genreId = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        Genre genre = genres.size() > 0 ? genres.get(0) : null;
        return genre;
    }

    public Collection<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery,
                this::mapRowToGenre).stream().sorted((o1, o2) -> o1.getId() - o2.getId()).collect(Collectors.toList());
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genreId"), resultSet.getString("name"));
    }
}
