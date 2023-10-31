package com.kodar.academy.Library.model.validation;

import com.kodar.academy.Library.repository.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueIsbnValidator implements ConstraintValidator<UniqueIsbn, String> {
    private final BookRepository bookRepository;
    public UniqueIsbnValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void initialize(UniqueIsbn constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return bookRepository.findByIsbn(s).orElse(null) == null;
    }
}
