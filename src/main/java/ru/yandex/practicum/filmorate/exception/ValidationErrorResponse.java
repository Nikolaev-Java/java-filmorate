package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ValidationErrorResponse {
    private final List<FieldValidationError> fieldValidationErrors = new ArrayList<>();

    public ValidationErrorResponse(String fieldName, String message) {
        fieldValidationErrors.add(new FieldValidationError(fieldName, message));
    }

    public ValidationErrorResponse(List<FieldValidationError> fieldValidationErrors) {
        this.fieldValidationErrors.addAll(fieldValidationErrors);
    }

}
