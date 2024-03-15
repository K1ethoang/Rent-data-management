package com.example.demo.utils;

import net.bytebuddy.utility.RandomString;

public class AuthUtils {
    private final static int DEFAULT_LENGTH_PASSWORD_GENERATOR = 10;

    public static String generatePassword() {
        return RandomString.make(DEFAULT_LENGTH_PASSWORD_GENERATOR);
    }
}
