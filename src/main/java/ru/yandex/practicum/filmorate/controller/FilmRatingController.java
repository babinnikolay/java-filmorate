package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.service.FilmRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class FilmRatingController {

    private final FilmRatingService service;

    @Autowired
    public FilmRatingController(FilmRatingService service) {
        this.service = service;
    }

    @GetMapping
    public List<FilmRating> getMpa() {
        return service.getAllMpa();
    }

    @GetMapping("/{id}")
    public FilmRating getMpaById(@PathVariable int id) throws MpaNotFoundException {
        return service.getMpsById(id);
    }
}
