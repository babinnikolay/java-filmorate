package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.List;

public interface FilmRatingService {

    List<FilmRating> getAllMpa();

    FilmRating getMpsById(int id) throws MpaNotFoundException;

}
