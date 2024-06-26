package com.example.demo.util;

import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUtils {
    public final static String REGEX_VALID_EMAIL = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+";
    /* kiểm tra username
     * Bắt đầu bằng chữ cái
     * Những ký tự còn lại có thể là chữ cái, chữ số và gạch dưới
     * Độ dài: nhỏ nhất là 5, lớn nhất là 25
     * */
    public final static String REGEX_VALID_USERNAME = "[A-Za-z][A-Za-z0-9_]{4,24}";
    public final static String REGEX_VALID_PHONE_NUMBER = "0(3[2-9]|5[689]|7[06-9]|8[1-68-9]|9[0-46-9])[0-9]{7}";
    public final static String REGEX_VALID_CITIZEN_ID = "\\d{12}";
    private final static int DEFAULT_LENGTH_PASSWORD_GENERATOR = 10;
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    public static String generatePassword() {
        return RandomString.make(DEFAULT_LENGTH_PASSWORD_GENERATOR);
    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = Pattern.compile(REGEX_VALID_EMAIL).matcher(email);

        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Matcher matcher = Pattern.compile(REGEX_VALID_PHONE_NUMBER).matcher(phoneNumber);

        return matcher.matches();
    }

    public static boolean isValidUsername(String username) {
        return Pattern.compile(REGEX_VALID_USERNAME)
                .matcher(username)
                .matches();
    }

    public static boolean isValidCitizenId(String citizenId) {
        return Pattern.compile(REGEX_VALID_CITIZEN_ID)
                .matcher(citizenId)
                .matches();
    }

    public static String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public static Boolean isValidPassword(String rawPassword, String encryptPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encryptPassword);
    }

}
