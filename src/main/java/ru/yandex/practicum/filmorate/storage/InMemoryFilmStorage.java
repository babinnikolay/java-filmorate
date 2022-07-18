package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public boolean contains(Film film) {
        return films.containsKey(film.getId());
    }

    @Override
    public Film getFilmById(Long id) throws FilmNotFoundException {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException();
        }
        return films.get(id);
    }

    @Override
    public int getStartId() {
        return 1;
    }

    @Override
    public Film addLike(Film film, User user) {
        film.addLike(user.getId());
        return film;
    }

    @Override
    public Film removeLike(Film film, User user) {
        film.removeLike(user.getId());
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Integer limit) {
        return films.values()
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
