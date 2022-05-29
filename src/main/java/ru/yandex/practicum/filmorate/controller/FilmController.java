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
        } else {
            log.error("film validation {}", film);
            throw new ValidationException();
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (isValid(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("update film {}", film);
        } else {
            log.error("film validation {}", film);
            throw new ValidationException();
        }
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    private boolean isValid(Film film) {
        return !film.getName().isEmpty()
                && film.getDescription().length() <= MAX_DESCRIPTION_LENGTH
                && (film.getReleaseDate().isAfter(FILMS_BIRTHDAY) || film.getReleaseDate().equals(FILMS_BIRTHDAY))
                && film.getDuration().toSeconds() > 0;
    }

}
