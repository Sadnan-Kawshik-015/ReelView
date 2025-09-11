package com.example.demo.utils.enums;

public enum VerificationMethod {
    EMAIL("email"), MOBILE_NUMBER("mobile_number");

    private final String value;

    VerificationMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static boolean isValid(String method) {
        for (VerificationMethod v : VerificationMethod.values()) {
            if (v.getValue().equals(method)) {
                return true;
            }
        }
        return false;
    }
}
