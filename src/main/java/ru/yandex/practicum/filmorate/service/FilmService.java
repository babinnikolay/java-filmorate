package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long filmNextId;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        filmNextId = filmStorage.getStartId();
    }

    public Film addFilm(Film film) throws ValidationException {
        film.setId(filmNextId);
        if (isValid(film)) {
            filmStorage.addFilm(film);
            filmNextId++;
            log.info("create new film {}", film);
        }
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException, FilmNotFoundException {
        if (!filmStorage.contains(film)) {
            String cause = "Film id not found";
            log.error("film validation {} cause - {}", film, cause);
            throw new FilmNotFoundException(cause);
        }
        if (isValid(film)) {
            filmStorage.updateFilm(film);
            log.info("update film {}", film);
        }
        return film;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
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
        if (film.getDuration() <= 0) {
            cause = "Duration is wrong";
            log.error("film validation {} cause - {}", film, cause);
            throw new ValidationException(cause);
        }
        return true;
    }

    public Film getFilmById(Long id) throws FilmNotFoundException {
        return filmStorage.getFilmById(id);
    }

    public Film addLike(Long id, Long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new FilmNotFoundException();
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        return filmStorage.addLike(film, user);
    }

    public Film removeLike(Long id, Long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new FilmNotFoundException();
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        return filmStorage.removeLike(film, user);
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getFilms()
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
