package ru.yandex.practicum.filmorate.exception;

public class FilmGenreNotFoundException extends Exception {

    public FilmGenreNotFoundException() {
    }

    public FilmGenreNotFoundException(String message) {
        super(message);
    }
}
