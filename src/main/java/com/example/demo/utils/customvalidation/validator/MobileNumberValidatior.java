package com.example.demo.utils.customvalidation.validator;

import com.example.demo.utils.customvalidation.annotation.MobileNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class MobileNumberValidatior implements ConstraintValidator<MobileNumber,String> {
    private static final String MOBILE_NUMBER_PATTERN = "(^(\\+8801){1}[3456789]{1}(\\d){8})$";
    private static final int MOBILE_NUMBER_LENGTH = 14;

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            if (input == null) {
                return false;
            }
            String fullPhoneNumber = "+" + input;

            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(fullPhoneNumber, "");
            String regionCodeForNumber = phoneNumberUtil.getRegionCodeForNumber(phoneNumber);

            if (!Objects.isNull(regionCodeForNumber)) {
                if (regionCodeForNumber.equals("BD")) {
                    return Pattern.matches(MOBILE_NUMBER_PATTERN, fullPhoneNumber) &&
                            fullPhoneNumber.length() == MOBILE_NUMBER_LENGTH;
                }
            }
            return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
