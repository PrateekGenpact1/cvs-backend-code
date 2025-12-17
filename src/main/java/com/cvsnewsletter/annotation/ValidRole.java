package com.cvsnewsletter.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    String message() default "Invalid role. Allowed values are USER, ADMIN, MANAGER (case-insensitive)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
