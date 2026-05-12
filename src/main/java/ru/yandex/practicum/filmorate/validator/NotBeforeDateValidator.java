package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.NotBeforeDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotBeforeDateValidator implements ConstraintValidator<NotBeforeDate, LocalDate> {
    private LocalDate minDate;

    @Override
    public void initialize(NotBeforeDate constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.value(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.isBefore(minDate);
    }
}
