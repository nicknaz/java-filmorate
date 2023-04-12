package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
	private final FriendsDbStorage friendsDbStorage;
	private final GenreDbStorage genreDbStorage;
	private final LikeDbStorage likeDbStorage;
	private final RatingDbStorage ratingDbStorage;

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
		assertThat(userStorage.getUserById(1).getFriends()).hasSize(1);
		assertThat(userStorage.getUserById(2).getFriends().get(1)).isNull();
		assertThat(userStorage.getUserById(1).getFriends().get(2)).isFalse();
	}

	@Test
	public void testConfirmedFriends() {
		userStorage.addUser(
				new User(3,"hi@jnh.ru", "second", "second", LocalDate.now(), null));
		friendsDbStorage.makeFriends(2, 3);
		friendsDbStorage.makeFriends(3, 2);
		assertThat(userStorage.getUserById(2).getFriends()).hasSize(1);
		assertThat(userStorage.getUserById(3).getFriends()).hasSize(1);
		assertThat(userStorage.getUserById(2).getFriends().get(3)).isTrue();
		assertThat(userStorage.getUserById(3).getFriends().get(2)).isTrue();
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
		Set genres = new HashSet<>();
		genres.add(new Genre(1));
		assertThat(filmDbStorage.addFilm(
				new Film(1,
						"HP",
						"desc",
						LocalDate.now(),
						120,
						null,
						genres,
						0,
						new Rating(1)))).hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	public void testFilmGetGenre() {
		assertThat(filmDbStorage
				.getFilmById(1)
				.getGenres()
				.contains(genreDbStorage.getGenreById(1))).isTrue();
	}

	@Test
	public void testFilmGetMPA() {
		assertThat(filmDbStorage
					.getFilmById(1)
					.getMpa()).hasFieldOrPropertyWithValue("name","G");
	}

	@Test
	public void testRemoveFilm() {
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
		assertThat(ratingDbStorage.getRatingById(1))
				.hasFieldOrPropertyWithValue("name", "G");
	}

	@Test
	public void testGetAllMPA() {
		assertThat(ratingDbStorage.getRatings())
				.hasSize(5);
	}

	@Test
	public void testGetGenre() {
		assertThat(genreDbStorage.getGenreById(1))
				.hasFieldOrPropertyWithValue("name", "Комедия");
	}

	@Test
	public void testGetAllGenres() {
		assertThat(genreDbStorage.getGenres())
				.hasSize(6);
	}

	@Test
	public void testLikeFilm() {
		likeDbStorage.likeFilm(1, 1);
		assertThat(filmDbStorage.getFilmById(1).getLikes()).hasSize(1);
	}

	@Test
	public void testUnlikeFilm() {
		likeDbStorage.unlikeFilm(1, 1);
		assertThat(filmDbStorage.getFilmById(1).getLikes()).hasSize(0);
	}
}
