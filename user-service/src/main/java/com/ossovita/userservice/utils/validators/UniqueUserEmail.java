package com.ossovita.userservice.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {UniqueUserEmailValidator.class})
public @interface UniqueUserEmail {

    String message() default "User email must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};



}