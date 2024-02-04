package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private int id;
    @NotBlank(message = "The field should not be empty")
    @NotNull(message = "The field should not be null")
    private String name;
    @Size(max = 200, message = "The description should be no more than 200 characters long")
    private String description;
    @ReleaseDate
    private LocalDate releaseDate;
    @NotNull(message = "The field should not be empty")
    @Positive(message = "The field must be positive")
    private long duration;
}
