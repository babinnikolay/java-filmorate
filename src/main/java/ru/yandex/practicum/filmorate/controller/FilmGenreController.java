package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.FilmGenreNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.serialize.FilmGenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class FilmGenreController {

    FilmGenreService service;

    @Autowired
    public FilmGenreController(FilmGenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<FilmGenre> getAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/{id}")
    public FilmGenre getGenreById(@PathVariable int id) throws FilmGenreNotFoundException {
        return service.getGenreById(id);
    }
}
