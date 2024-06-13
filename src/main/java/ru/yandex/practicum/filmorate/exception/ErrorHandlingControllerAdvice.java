package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcBatchUpdateException;
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
        final List<FieldValidationError> fieldValidationErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    log.warn(fieldError.getObjectName() + "." + fieldError.getField()
                            + " - " + fieldError.getDefaultMessage());
                    return new FieldValidationError(fieldError.getField(), fieldError.getDefaultMessage());
                })
                .collect(Collectors.toList());
        return new ValidationErrorResponse(fieldValidationErrors);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ValidationErrorResponse onNotFound(NotFoundException e) {
        log.warn(e.getObjectName() + " - " + e.getMessage());
        return new ValidationErrorResponse(List.of(new FieldValidationError(e.getObjectName(), e.getMessage())));
    }
    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onInternalServerException(InternalServerException e) {
        return new ValidationErrorResponse(List.of(new FieldValidationError("sql", e.getMessage())));
    }
}
