package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ValidationErrorResponse {
    private final List<Error> errors = new ArrayList<>();

    public ValidationErrorResponse(String fieldName, String message) {
        errors.add(new Error(fieldName,message));
    }

    public ValidationErrorResponse(List<Error> errors) {
        this.errors.addAll(errors);
    }

}
