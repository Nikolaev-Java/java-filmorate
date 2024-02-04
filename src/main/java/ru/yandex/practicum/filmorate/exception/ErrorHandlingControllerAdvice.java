package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Error> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    log.warn(fieldError.getObjectName() + "." + fieldError.getField()
                            + " - " + fieldError.getDefaultMessage());
                    return new Error(fieldError.getField(), fieldError.getDefaultMessage());
                })
                .collect(Collectors.toList());
        return new ValidationErrorResponse(errors);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error onObjectNotFound(ObjectNotFoundException e) {
        log.warn(e.getObjectName() + " - " + e.getMessage());
        return new Error(e.getObjectName(), e.getMessage());
    }
}
