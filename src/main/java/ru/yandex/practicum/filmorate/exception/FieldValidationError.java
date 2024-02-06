package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FieldValidationError {

    private String fieldName;

    private String message;

    public FieldValidationError(@JsonProperty("fieldName") String fieldName, @JsonProperty("message") String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
