package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.FilmGenreNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreService {
    List<FilmGenre> getAllGenres();

    FilmGenre getGenreById(int id) throws FilmGenreNotFoundException;
}
