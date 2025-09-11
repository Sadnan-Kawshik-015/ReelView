package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OTPUtils {
    @Value("${app.otpExpiryMinutes}")
    private int otp_expiry_minutes;
    @Value("${app.verificationTokenExpiryMinutes}")
    private int verification_otp_expiry_minutes;

    public String OTP(int len) {
        String numbers = "0123456789";
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(new SecureRandom().nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }

    public int getOTPExpiry() {

        return Integer.valueOf(otp_expiry_minutes);
    }
    public int getVerificationOTPExpiry() {

        return Integer.valueOf(verification_otp_expiry_minutes);
    }
}
