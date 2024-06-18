package ru.yandex.practicum.filmorate.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
@Documented
public @interface ReleaseDate {
    String message() default "The release date is not earlier than December 28, 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
