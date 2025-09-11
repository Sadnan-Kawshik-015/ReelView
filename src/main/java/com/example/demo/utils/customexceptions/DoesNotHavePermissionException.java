package com.example.demo.utils.customexceptions;

public class DoesNotHavePermissionException extends Exception {
    public DoesNotHavePermissionException(String message) {
        super(message);
    }
}
