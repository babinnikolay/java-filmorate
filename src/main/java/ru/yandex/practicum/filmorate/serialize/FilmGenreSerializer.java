package ru.yandex.practicum.filmorate.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.io.IOException;

public class FilmGenreSerializer extends StdSerializer<FilmGenre> {
    protected FilmGenreSerializer() {
        super(FilmGenre.class);
    }

    @Override
    public void serialize(FilmGenre filmGenre,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("id");
        generator.writeNumber(filmGenre.ordinal() + 1);
        generator.writeFieldName("name");
        generator.writeString(filmGenre.getTitle());
        generator.writeEndObject();
    }
}
