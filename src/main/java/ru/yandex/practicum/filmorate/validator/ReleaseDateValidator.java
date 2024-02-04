package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.Format;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        return !localDate.isBefore(LocalDate.of(1895,12,28));
    }
}
