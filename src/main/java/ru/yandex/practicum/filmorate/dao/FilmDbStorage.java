package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    FilmGenreDbStorage filmGenreDbStorage;
    FilmRatingDbStorage filmRatingDbStorage;
    LikeDbStorage likeDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         @Autowired FilmGenreDbStorage filmGenreDbStorage,
                         @Autowired FilmRatingDbStorage filmRatingDbStorage,
                         @Autowired LikeDbStorage likeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDbStorage = filmGenreDbStorage;
        this.filmRatingDbStorage = filmRatingDbStorage;
        this.likeDbStorage = likeDbStorage;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films(name, description, duration, releaseDate, ratingId) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"filmId"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        if (film.getGenres() != null) {
            filmGenreDbStorage.addFilmGenres(film.getId(), film.getGenres());
        }
        film.setMpa(filmRatingDbStorage.getRating(film.getMpa().getId()));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films" +
                " SET name = ?," +
                " description = ?," +
                " duration = ?," +
                " releaseDate = ?," +
                " ratingId = ?," +
                " rate = ?" +
                " WHERE filmId = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                Date.valueOf(film.getReleaseDate()),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());
        filmGenreDbStorage.updateFilmGenres(film.getId(), film.getGenres());
        return getFilmById(film.getId());
    }

    @Override
    public Set<Film> getFilmsSet() {
        String sqlQuery = "SELECT filmId, name, description, duration, releaseDate, ratingId FROM films";
        Set<Film> films = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToFilm));
        return films;
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "SELECT filmId, name, description, duration, releaseDate, ratingId FROM films WHERE filmId = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
        Film film = films.size() > 0 ? films.get(0) : null;
        return film;
    }

    @Override
    public void removeAllFilms() {
        String sqlQuery = "DELETE FROM films";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public void removeFilm(int id) {
        String sqlQuery = "DELETE FROM films WHERE filmId = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("filmId"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .build();
        if (film.getGenres() != null) {
            film.setGenres(new HashSet<>(filmGenreDbStorage.getFilmGenres(film.getId())));
        }
        film.setMpa(filmRatingDbStorage.getRating(resultSet.getInt("ratingId")));
        film.setLikes(likeDbStorage.getFilmLikes(film.getId()));
        return film;
    }
}
