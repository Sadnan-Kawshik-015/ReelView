package com.example.demo.utils.customvalidation.validator;

import com.example.demo.utils.customvalidation.annotation.DateValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DateValidator implements ConstraintValidator<DateValidation,String> {
    private final String DATE_PATTERN = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null) {
            return false;
        }
        return Pattern.matches(DATE_PATTERN, input);
    }
}
