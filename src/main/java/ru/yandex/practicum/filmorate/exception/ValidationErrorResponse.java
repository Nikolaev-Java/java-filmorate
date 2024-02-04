package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ValidationErrorResponse {
    private final List<Error> errors = new ArrayList<>();

    public ValidationErrorResponse(String fieldName, String message) {
        errors.add(new Error(fieldName, message));
    }

    public ValidationErrorResponse(List<Error> errors) {
        this.errors.addAll(errors);
    }

}
