package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private long id;

    @NotBlank
    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate releaseDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @NotNull
    private Duration duration;
}
