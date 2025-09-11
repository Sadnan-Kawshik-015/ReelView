package com.example.demo.utils.enums;

public enum Status {
    SUCCESS("success"), ERROR("error");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
