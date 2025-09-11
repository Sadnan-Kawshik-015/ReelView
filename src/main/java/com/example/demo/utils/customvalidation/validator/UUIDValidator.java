package com.example.demo.utils.customvalidation.validator;

import com.example.demo.utils.customvalidation.annotation.UUID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UUIDValidator implements ConstraintValidator<UUID, String> {
    private static final String UUID_PATTERN = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null) {
            return false;
        }
        return Pattern.matches(UUID_PATTERN, input);
    }
}
