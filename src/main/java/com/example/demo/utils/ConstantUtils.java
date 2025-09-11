package com.example.demo.utils;

import java.util.UUID;

public class ConstantUtils {
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
