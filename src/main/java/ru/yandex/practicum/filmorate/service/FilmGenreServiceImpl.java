package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmGenreNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Arrays;
import java.util.List;

@Service
public class FilmGenreServiceImpl implements FilmGenreService {

    @Override
    public List<FilmGenre> getAllGenres() {
        return Arrays.asList(FilmGenre.values());
    }

    @Override
    public FilmGenre getGenreById(int id) throws FilmGenreNotFoundException {
        if (id < 0 || id > FilmGenre.values().length - 1) {
            throw new FilmGenreNotFoundException();
        }
        return FilmGenre.values()[id - 1];
    }
}
