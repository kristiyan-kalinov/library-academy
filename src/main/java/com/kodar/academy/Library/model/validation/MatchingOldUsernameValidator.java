package com.kodar.academy.Library.model.validation;

import com.kodar.academy.Library.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchingOldUsernameValidator implements ConstraintValidator<MatchingOldUsername, String> {

    private UserRepository userRepository;

    public MatchingOldUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(MatchingOldUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String oldUsername = "";
        if(userRepository.findByUsername(s).isPresent()) {
            oldUsername = userRepository.findByUsername(s).orElse(null).getUsername();
        }
        return s.equals(oldUsername) || userRepository.findByUsername(s).orElse(null) == null;
    }
}
