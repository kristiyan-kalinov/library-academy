package com.kodar.academy.Library.model.validation;

import com.kodar.academy.Library.model.constants.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingGenreValidator.class)
public @interface ExistingGenre {
    String message() default Constants.GENRE_NOT_EXISTING;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
