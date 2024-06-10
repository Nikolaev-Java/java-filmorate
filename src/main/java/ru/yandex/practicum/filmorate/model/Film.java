package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
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
    private Set<Integer> likes = new HashSet<>();

    public void addLike(int idUser) {
        likes.add(idUser);
    }

    public void removeLike(int idUser) {
        likes.remove(idUser);
    }
}
