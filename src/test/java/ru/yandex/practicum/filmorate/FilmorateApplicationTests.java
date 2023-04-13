package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;
	private final FilmDbStorage filmDbStorage;
	private final FilmRatingDbStorage filmRatingDbStorage;
	private final FilmGenreDbStorage filmGenreDbStorage;
	private final FriendsDbStorage friendsDbStorage;
	private final GenreDbStorage genreDbStorage;
	private final LikeDbStorage likeDbStorage;
	private final RatingDbStorage ratingDbStorage;

	@Autowired
	private ApplicationContext context;


	@Test
	public void testAddNewUser() {
		assertThat(userStorage.addUser(
				new User(1,"hi@jnh.ru", "jd", "fwcw", LocalDate.now(), null)))
				.hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	public void testFindUserById() {
		User user = userStorage.getUserById(1);

		assertThat(user).hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	public void testFindAllUser() {
		userStorage.addUser(
				new User(2,"hi@jnh.ru", "second", "second", LocalDate.now(), null));
		Set<User> user = userStorage.getUsersSet();

		assertThat(user).hasSize(2);
	}

	@Test
	public void testUpdateUser() {
		assertThat(userStorage.updateUser(
				new User(1,"hi@jnh.ru", "hhh", "fwcw", LocalDate.now(), null)))
				.hasFieldOrPropertyWithValue("login", "hhh");
	}

	@Test
	public void testUnconfirmedFriends() {
		friendsDbStorage.makeFriends(1, 2);
		User user1 = userStorage.getUserById(1);
		User user2 = userStorage.getUserById(2);
		initFriends(user1);
		initFriends(user2);
		assertThat(user1.getFriends()).hasSize(1);
		assertThat(user2.getFriends().get(1)).isNull();
		assertThat(user1.getFriends().get(2)).isFalse();
	}

	@Test
	public void testConfirmedFriends() {
		userStorage.addUser(
				new User(3,"hi@jnh.ru", "second", "second", LocalDate.now(), null));
		friendsDbStorage.makeFriends(2, 3);
		friendsDbStorage.makeFriends(3, 2);
		User user2 = userStorage.getUserById(2);
		User user3 = userStorage.getUserById(3);
		initFriends(user2);
		initFriends(user3);
		assertThat(user2.getFriends()).hasSize(1);
		assertThat(user3.getFriends()).hasSize(1);
		assertThat(user2.getFriends().get(3)).isTrue();
		assertThat(user3.getFriends().get(2)).isTrue();
	}

	@Test
	public void testRemoveUser() {
		userStorage.addUser(
				new User(4,"hi@jnh.ru", "second", "second", LocalDate.now(), null));
		assertThat(userStorage.getUserById(4)).isNotNull();
		userStorage.removeUser(4);
		assertThat(userStorage.getUserById(4)).isNull();
	}

	@Test
	public void testRemoveAllUser() {
		userStorage.removeAllUsers();
		assertThat(userStorage.getUsersSet()).hasSize(0);
	}

	@Test
	public void testAddFilm() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		Set genres = new HashSet<>();
		genres.add(new Genre(1));
		Film film = filmDbStorage.addFilm(
				new Film(1,
						"HP",
						"desc",
						LocalDate.now(),
						120,
						null,
						genres,
						0,
						new Rating(1)));
		filmGenreDbStorage.updateFilmGenres(film.getId(), genres);
		initLikesAndGenres(film);
		assertThat(film).hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	public void testFilmGetGenre() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		Film film = filmDbStorage.getFilmById(1);
		initLikesAndGenres(film);
		assertThat(film
				.getGenres()
				.contains(genreDbStorage.getGenreById(1))).isTrue();
	}

	@Test
	public void testFilmGetMPA() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		assertThat(filmDbStorage
					.getFilmById(1)
					.getMpa()).hasFieldOrPropertyWithValue("name","G");
	}

	@Test
	public void testRemoveFilm() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		Set genres = new HashSet<>();
		genres.add(new Genre(1));
		filmDbStorage.addFilm(
				new Film(2,
						"HP",
						"desc",
						LocalDate.now(),
						120,
						null,
						genres,
						0,
						new Rating(1)));
		assertThat(filmDbStorage.getFilmsSet()).hasSize(2);
		filmDbStorage.removeFilm(2);
		assertThat(filmDbStorage.getFilmsSet()).hasSize(1);
	}

	@Test
	public void testGetMPA() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		assertThat(ratingDbStorage.getRatingById(1))
				.hasFieldOrPropertyWithValue("name", "G");
	}

	@Test
	public void testGetAllMPA() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		assertThat(ratingDbStorage.getRatings())
				.hasSize(5);
	}

	@Test
	public void testGetGenre() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		assertThat(genreDbStorage.getGenreById(1))
				.hasFieldOrPropertyWithValue("name", "Комедия");
	}

	@Test
	public void testGetAllGenres() {
		Rating.setApplicationContext(context);
		Genre.setApplicationContext(context);
		assertThat(genreDbStorage.getGenres())
				.hasSize(6);
	}

	@Test
	public void testLikeFilm() {
		likeDbStorage.likeFilm(1, 1);
		Film film = filmDbStorage.getFilmById(1);
		initLikesAndGenres(film);
		assertThat(film.getLikes()).hasSize(1);
	}

	@Test
	public void testUnlikeFilm() {
		likeDbStorage.unlikeFilm(1, 1);
		assertThat(filmDbStorage.getFilmById(1).getLikes()).hasSize(0);
	}

	private void initLikesAndGenres(Set<Film> films) {
		for (Film film : films) {
			initLikesAndGenres(film);
		}
	}

	private void initLikesAndGenres(Film film) {
		film.setGenres(filmGenreDbStorage.getFilmGenres(film.getId()));
		film.setLikes(likeDbStorage.getFilmLikes(film.getId()));
	}

	private void initFriends(Set<User> users) {
		for (User user: users) {
			initFriends(user);
		}
	}

	private void initFriends(User user) {
		SqlRowSet rowSet = friendsDbStorage.getAllFriends(user.getId());
		while (rowSet.next()) {
			if (rowSet.getInt("userID") == user.getId()) {
				user.getFriends().put(rowSet.getInt("friendId"), rowSet.getBoolean("status"));
			} else if (rowSet.getBoolean("status")) {
				user.getFriends().put(rowSet.getInt("userId"), rowSet.getBoolean("status"));
			}
		}
	}
}
