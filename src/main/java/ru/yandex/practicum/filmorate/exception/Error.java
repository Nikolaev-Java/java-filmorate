package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class Error {

    private String fieldName;

    private String message;

    public Error(@JsonProperty("fieldName") String fieldName, @JsonProperty("message") String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
