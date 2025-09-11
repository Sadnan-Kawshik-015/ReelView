package com.example.demo.utils.customvalidation.annotation;

import com.example.demo.utils.customvalidation.validator.DateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface DateValidation {
    String message() default "Invalid Date Format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
