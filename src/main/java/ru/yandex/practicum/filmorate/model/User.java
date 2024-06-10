package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    /*private Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }*/
}
