package ru.yandex.practicum.filmorate.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.io.IOException;

public class FilmRatingSerializer extends StdSerializer<FilmRating> {

    protected FilmRatingSerializer() {
        super(FilmRating.class);
    }

    @Override
    public void serialize(FilmRating filmRating,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {

        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeNumber(filmRating.ordinal() + 1);
        generator.writeFieldName("name");
        generator.writeString(filmRating.getTitle());
        generator.writeEndObject();

    }
}
