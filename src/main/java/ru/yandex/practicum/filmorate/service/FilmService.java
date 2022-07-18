package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException, FilmNotFoundException;

    Collection<Film> getFilms();

    Film getFilmById(Long id) throws FilmNotFoundException;

    Film addLike(Long id, Long userId) throws FilmNotFoundException, UserNotFoundException;

    Film removeLike(Long id, Long userId) throws FilmNotFoundException, UserNotFoundException;

    List<Film> getPopular(Integer count);
}
