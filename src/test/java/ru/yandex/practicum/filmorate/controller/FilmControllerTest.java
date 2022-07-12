package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;
    private Film film;

    @BeforeEach
    public void setUp() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmServiceImpl(filmStorage, userStorage);
        filmController = new FilmController(filmService);
        film = new Film();
        film.setId(1);
        film.setDescription("desc");
        film.setDuration((180));
        film.setName("name");
        film.setReleaseDate(LocalDate.of(1900, 10, 10));
    }

    @Test
    public void shouldThrowValidationExceptionWhenNameIsEmpty() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldThrowValidationExceptionWhenDescriptionLengthIsTooLong() {
        film.setDescription("a".repeat(201));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldAddNewFilmWhenDescriptionIsNormalLength() {
        film.setDescription("a".repeat(200));
        assertDoesNotThrow(() -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void shouldThrowValidationExceptionWhenReleaseDateIsTooOld() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldAddNewFilmWhenReleaseDateIsNormal() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void shouldThrowValidationExceptionWhenDurationIsNegative() {
        film.setDuration((-1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldThrowValidationExceptionWhenDurationIsZero() {
        film.setDuration((0));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldAddNewFilmWhenDurationIsPositive() {
        film.setDuration((1));
        assertDoesNotThrow(() -> filmController.addFilm(film));
    }

}