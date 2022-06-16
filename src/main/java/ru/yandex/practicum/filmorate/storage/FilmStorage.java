package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void addFilm(Film film);
    void updateFilm(Film film);
    Collection<Film> getFilms();
    boolean contains(Film film);
    Film getFilmById(Long id) throws FilmNotFoundException;
}
