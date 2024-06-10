package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.time.LocalDate;

@Data
public class User {
    @DecimalMin(value = "0", message = "The field must be a number")
    private int id;
    @NotBlank(message = "The field should not be empty")
    @NotNull(message = "The field should not be null")
    @Email(message = "The email is not correct")
    private String email;
    @Pattern(regexp = "^\\S+$", message = "The login must not contain spaces or not be empty")
    @NotNull(message = "The field should not be null")
    private String login;
    private String name;
    @Past(message = "The date of birth should not be in the future")
    @NotNull(message = "The field should not be null")
    private LocalDate birthday;
}
