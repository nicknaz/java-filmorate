package ru.yandex.practicum.filmorate.annotation.validator;

import ru.yandex.practicum.filmorate.annotation.FilmDatePositive;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmDatePositiveValidator implements ConstraintValidator<FilmDatePositive, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if(date.isBefore(LocalDate.of(1895, 12,28))){
            return false;
        }
        return true;
    }
}
