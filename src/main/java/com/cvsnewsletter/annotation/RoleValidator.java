package com.cvsnewsletter.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    private static final Set<String> VALID_ROLES = Set.of("USER", "ADMIN", "MANAGER");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_ROLES.contains(value.toUpperCase());
    }
}
