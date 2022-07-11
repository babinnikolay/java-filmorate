package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException handleValidationException(final ValidationException e) {
        return new ValidationException("Ошибка валидации");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserNotFoundException handleUserNotFoundException(final UserNotFoundException e) {
        return new UserNotFoundException("Пользователь не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public FilmNotFoundException handleFilmNotFoundException(final FilmNotFoundException e) {
        return new FilmNotFoundException("Фильм не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MpaNotFoundException handleMpaNotFoundException(final MpaNotFoundException e) {
        return new MpaNotFoundException("Рейтинг MPA не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public FilmGenreNotFoundException handleFilmGenreNotFoundException(final FilmGenreNotFoundException e) {
        return new FilmGenreNotFoundException("Жанр не найден");
    }
}
