package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    void updateFilm(Film film);

    Collection<Film> getFilms();

    boolean contains(Film film);

    Film getFilmById(Long id) throws FilmNotFoundException;

    int getStartId();

    Film addLike(Film film, User user);

    Film removeLike(Film film, User user);

    List<Film> getPopularFilms(Integer limit);
}
