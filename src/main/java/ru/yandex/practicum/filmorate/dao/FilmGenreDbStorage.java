package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundedException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class FilmGenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getFilmGenres(int filmId){
        String sqlQuery = "SELECT * FROM FilmGenre AS fg " +
                "LEFT OUTER JOIN Genres as g ON fg.genreId = g.genreId" +
                " WHERE fg.filmId = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
        return genres;
    }

    public int getGenreIdByName(String name){
        String sqlQuery = "SELECT * FROM Genres" +
                " WHERE name = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, name);
        if (rowSet.next()) {
            Integer genreId = rowSet.getInt("genreId");
            return genreId;
        } else {
            throw new NotFoundedException("Жанр не найден");
        }
    }

    public void addFilmGenres(int filmId, Collection<Genre> genres){
        String sqlQuery = "INSERT INTO FilmGenre(filmId, genreId) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery, filmId, genre.getId());
        }
    }

    public void removeFilmGenres(int filmId){
        String sqlQuery = "DELETE FROM FilmGenre WHERE filmId = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public void updateFilmGenres(int filmId, Collection<Genre> genres){
        removeFilmGenres(filmId);
        addFilmGenres(filmId, genres);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genreId"), resultSet.getString("name"));
    }
}
