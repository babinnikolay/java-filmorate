package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends Exception {
    public FilmNotFoundException() {
    }

    public FilmNotFoundException(String message) {
        super(message);
    }
}
