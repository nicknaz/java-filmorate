package ru.yandex.practicum.filmorate.annotation.validator;

import ru.yandex.practicum.filmorate.annotation.DurationPositive;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class DurationPositiveValidator implements ConstraintValidator<DurationPositive, Duration> {

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext constraintValidatorContext) {
        if(duration.isNegative()){
            return false;
        }
        return false;
    }
}
