package ru.yandex.practicum.filmorate.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.io.IOException;

public class FilmGenreDeserializer extends StdDeserializer<FilmGenre> {
    protected FilmGenreDeserializer() {
        super(FilmGenre.class);
    }

    @Override
    public FilmGenre deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        int filmGenreId = node.get("id").asInt();
        return FilmGenre.values()[filmGenreId - 1];
    }
}
