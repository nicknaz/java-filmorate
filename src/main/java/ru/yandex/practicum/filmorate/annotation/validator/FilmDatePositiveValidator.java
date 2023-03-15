package ru.yandex.practicum.filmorate.annotation.validator;

import ru.yandex.practicum.filmorate.annotation.DurationPositive;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDate;

public class FilmDatePositiveValidator implements ConstraintValidator<DurationPositive, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if(date.isBefore(LocalDate.of(1895, 12,28))){
            return false;
        }
        return false;
    }
}
