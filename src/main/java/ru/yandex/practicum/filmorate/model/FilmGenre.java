package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.yandex.practicum.filmorate.serialize.FilmGenreDeserializer;
import ru.yandex.practicum.filmorate.serialize.FilmGenreSerializer;

@JsonSerialize(using = FilmGenreSerializer.class)
@JsonDeserialize(using = FilmGenreDeserializer.class)
public enum FilmGenre {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    CARTOON("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик");

    private final String title;

    FilmGenre(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }
}
