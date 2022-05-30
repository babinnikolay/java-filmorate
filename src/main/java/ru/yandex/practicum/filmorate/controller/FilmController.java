package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        film.setId(nextId++);

        if (isValid(film)) {
            films.put(film.getId(), film);
            log.info("create new film {}", film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            String cause = "Film id not found";
            log.error("film validation {} cause - {}", film, cause);
            throw new ValidationException(cause);
        }
        if (isValid(film)) {
            films.put(film.getId(), film);
            log.info("update film {}", film);
        }
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    private boolean isValid(Film film) throws ValidationException {
        String cause;
        if (film.getName().isEmpty()) {
            cause = "Name is empty";
            log.error("film validation {} cause - {}", film, cause);
            throw new ValidationException(cause);
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            cause = "Description is too long";
            log.error("film validation {} cause - {}", film, cause);
            throw new ValidationException(cause);
        }
        if (!(film.getReleaseDate().isAfter(FILMS_BIRTHDAY) || film.getReleaseDate().equals(FILMS_BIRTHDAY))) {
            cause = "Release date is wrong";
            log.error("film validation {} cause - {}", film, cause);
            throw new ValidationException(cause);
        }
        if (film.getDuration().toSeconds() <= 0) {
            cause = "Duration is wrong";
            log.error("film validation {} cause - {}", film, cause);
            throw new ValidationException(cause);
        }
        return true;
    }

}
