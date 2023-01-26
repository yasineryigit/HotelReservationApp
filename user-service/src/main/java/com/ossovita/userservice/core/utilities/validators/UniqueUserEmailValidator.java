package com.ossovita.userservice.core.utilities.validators;

import com.ossovita.userservice.core.dataAccess.UserRepository;
import com.ossovita.userservice.core.entities.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String userEmail, ConstraintValidatorContext context) {
        User user = userRepository.findUserByUserEmail(userEmail);
        if (user != null) {
            return false;
        }
        return true;
    }
}