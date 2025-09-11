package com.example.demo.utils.customexceptions;

public class RefreshTokenNotValidException extends Exception {
    public RefreshTokenNotValidException(String message) {
        super(message);
    }
}
