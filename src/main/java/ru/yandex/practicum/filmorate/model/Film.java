package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {
    private long id;
    private List<Long> likes = new ArrayList<>();
    private List<FilmGenre> genres = new ArrayList<>();
    private FilmRating mpa;

    @NotBlank
    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate releaseDate;

    @NotNull
    private int duration;

    public Film() {
    }

    public Film(long id, FilmRating mpa, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.mpa = mpa;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }
}
