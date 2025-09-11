package com.example.demo.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodUtils {
    @Value("${app.forgetPwdTokenExpiryMinutes}")
    private int token_expiry_minutes;

    public int getForgetPwdTokenExpiry() {

        return Integer.valueOf(token_expiry_minutes);
    }
}
