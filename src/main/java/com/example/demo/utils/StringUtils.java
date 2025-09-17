package com.example.demo.utils;

public class StringUtils {
    public static String formatName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1 $2");
    }
}
