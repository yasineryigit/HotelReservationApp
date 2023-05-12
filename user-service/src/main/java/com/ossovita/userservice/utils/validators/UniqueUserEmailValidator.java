package com.ossovita.userservice.utils.validators;

import com.ossovita.userservice.repository.UserRepository;
import com.ossovita.userservice.entity.User;

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
        return user.isEmpty();
    }
}
