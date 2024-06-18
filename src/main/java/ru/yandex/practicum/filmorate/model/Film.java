package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = "id")
public class Film {
    @DecimalMin(value = "0", message = "The field must be a number")
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
    private Mpa mpa;
    @JsonDeserialize(as = LinkedHashSet.class)
    private Set<Genre> genres = new LinkedHashSet<>();

}
