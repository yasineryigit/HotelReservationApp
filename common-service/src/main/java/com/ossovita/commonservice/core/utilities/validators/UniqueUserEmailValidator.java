package com.ossovita.commonservice.core.utilities.validators;

import com.ossovita.commonservice.core.dataAccess.UserRepository;
import com.ossovita.commonservice.core.entities.User;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String userEmail, ConstraintValidatorContext context) {
        Optional<User> user = userRepository.findByUserEmail(userEmail);
        if (user.isPresent()) {
            return false;
        }
        return true;
    }
}
