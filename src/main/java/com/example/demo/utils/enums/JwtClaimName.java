package com.example.demo.utils.enums;

public enum JwtClaimName {
    ID("id"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    AUTHORITIES("authorities");

    private final String value;

    private JwtClaimName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
