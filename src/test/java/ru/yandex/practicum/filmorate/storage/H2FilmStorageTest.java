package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class H2FilmStorageTest {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Test
    public void testAddNewFilm() throws FilmNotFoundException {
        long id = 3;
        String name = "";
        String description = "";
        int duration = 120;
        LocalDate releaseDate = LocalDate.now();
        FilmRating filmRating = FilmRating.G;

        filmStorage.addFilm(new Film(id, filmRating, name, description, releaseDate, duration));

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(3L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 3L)
                );
    }

    @Test
    public void testUpdateFilm() throws FilmNotFoundException {
        long id = 1;
        String name = "new name";
        String description = "";
        int duration = 120;
        LocalDate releaseDate = LocalDate.now();
        FilmRating filmRating = FilmRating.G;

        filmStorage.updateFilm(new Film(id, filmRating, name, description, releaseDate, duration));

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "new name")
                );
    }

    @Test
    public void testGetAllFilm() {

        Optional<Collection<Film>> filmOptional = Optional.ofNullable(filmStorage.getFilms());

        assertThat(filmOptional)
                .isPresent()
                .isNotEmpty();
    }

    @Test
    public void testContainsFilm() {

        long id = 1;
        String name = "";
        String description = "";
        int duration = 120;
        LocalDate releaseDate = LocalDate.now();
        FilmRating filmRating = FilmRating.G;

        Film film = new Film(id, filmRating, name, description, releaseDate, duration);


        assertTrue("", filmStorage.contains(film));

    }

    @Test
    public void testGetFilmById() throws FilmNotFoundException {

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testGetStartId() {

        Optional<Integer> idOptional = Optional.of(filmStorage.getStartId());
        assertThat(idOptional)
                .isPresent()
                .hasValue(3);
    }

    @Test
    public void testAddFilmLike() {

        long id = 3;
        String name = "";
        String description = "";
        int duration = 120;
        LocalDate releaseDate = LocalDate.now();
        FilmRating filmRating = FilmRating.G;

        Film newFilm = new Film(id, filmRating, name, description, releaseDate, duration);

        String email = "";
        String login = "";
        LocalDate birthday = LocalDate.now();
        User user = new User(1L, email, login, "", birthday);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addLike(newFilm, user));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes().get(0)).isEqualTo(1L));

    }

    @Test
    public void testRemoveLike() {

        long id = 1;
        String name = "";
        String description = "";
        int duration = 120;
        LocalDate releaseDate = LocalDate.now();
        FilmRating filmRating = FilmRating.G;

        Film newFilm = new Film(id, filmRating, name, description, releaseDate, duration);

        String email = "";
        String login = "";
        LocalDate birthday = LocalDate.now();
        User user = new User(12L, email, login, "", birthday);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.removeLike(newFilm, user));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes().size()).isZero());

    }

}