package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.Arrays;
import java.util.List;

@Service
public class FilmRatingService {

    public List<FilmRating> getAllMpa() {
        return Arrays.asList(FilmRating.values());
    }


    public FilmRating getMpsById(int id) throws MpaNotFoundException {
        if (id < 0 || id > FilmRating.values().length - 1) {
            throw new MpaNotFoundException();
        }
        return FilmRating.values()[id - 1];
    }
}
