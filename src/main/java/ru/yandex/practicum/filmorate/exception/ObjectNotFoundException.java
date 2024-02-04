package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ObjectNotFoundException extends RuntimeException {
    private final String message;
    private final String objectName;
}
