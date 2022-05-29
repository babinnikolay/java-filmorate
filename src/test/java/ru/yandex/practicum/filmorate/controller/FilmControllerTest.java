package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmController filmController;
    private Film film;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
        film = new Film();
        film.setId(1);
        film.setDescription("desc");
        film.setDuration(Duration.ofMinutes(180));
        film.setName("name");
        film.setReleaseDate(LocalDate.of(1900, 10, 10));
    }

    @Test
    public void shouldThrowValidationExceptionWhenNameIsEmpty() {
        film.setName("");
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void shouldThrowValidationExceptionWhenDescriptionLengthIsTooLong() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            builder.append("a");
        }
        film.setDescription(builder.toString());
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldAddNewFilmWhenDescriptionIsNormalLength() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            builder.append("a");
        }
        film.setDescription(builder.toString());
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
        film.setDuration(Duration.ofMinutes(-1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldThrowValidationExceptionWhenDurationIsZero() {
        film.setDuration(Duration.ofMinutes(0));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void shouldAddNewFilmWhenDurationIsPositive() {
        film.setDuration(Duration.ofMinutes(1));
        assertDoesNotThrow(() -> filmController.addFilm(film));
    }

}