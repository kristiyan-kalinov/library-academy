package com.kodar.academy.Library.model.validation;

import com.kodar.academy.Library.repository.GenreRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistingGenreValidator implements ConstraintValidator<ExistingGenre, String> {
    private final GenreRepository genreRepository;
    public ExistingGenreValidator(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void initialize(ExistingGenre constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return genreRepository.findByName(s).isPresent();
    }
}
