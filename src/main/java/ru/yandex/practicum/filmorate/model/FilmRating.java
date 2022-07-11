package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.yandex.practicum.filmorate.serialize.FilmRatingDeserializer;
import ru.yandex.practicum.filmorate.serialize.FilmRatingSerializer;

@JsonSerialize(using = FilmRatingSerializer.class)
@JsonDeserialize(using = FilmRatingDeserializer.class)
public enum FilmRating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");

    private final String title;

    FilmRating(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }
}
