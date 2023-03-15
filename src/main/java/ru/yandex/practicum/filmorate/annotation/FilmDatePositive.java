package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.annotation.validator.DurationPositiveValidator;
import ru.yandex.practicum.filmorate.annotation.validator.FilmDatePositiveValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = FilmDatePositiveValidator.class)
@Documented
public @interface FilmDatePositive {

    String message() default "{FilmDatePositive.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
