package ru.yandex.practicum.filmorate.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.io.IOException;

public class FilmRatingDeserializer extends StdDeserializer<FilmRating> {

    protected FilmRatingDeserializer() {
        super(FilmRating.class);
    }

    @Override
    public FilmRating deserialize(JsonParser jsonParser,
                                  DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        int filmRatingId = node.get("id").asInt();
        return FilmRating.values()[filmRatingId - 1];

    }
}
