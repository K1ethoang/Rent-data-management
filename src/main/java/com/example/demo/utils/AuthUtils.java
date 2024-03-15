package com.example.demo.utils;

import net.bytebuddy.utility.RandomString;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUtils {
    public final static String REGEX_VALID_EMAIL = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    /* kiểm tra username
     * Bắt đầu bằng chữ cái
     * Những ký tự còn lại có thể là chữ cái, chữ số và gạch dưới
     * Độ dài: nhỏ nhất là 8, lớn nhất là 25
     * */
    public final static String REGEX_VALID_USERNAME = "^[A-Za-z][A-Za-z0-9_]{7,24}$";
    private final static int DEFAULT_LENGTH_PASSWORD_GENERATOR = 10;

    public static String generatePassword() {
        return RandomString.make(DEFAULT_LENGTH_PASSWORD_GENERATOR);
    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = Pattern.compile(REGEX_VALID_EMAIL).matcher(email);

        return matcher.matches();
    }

    public static boolean isValidUsername(String username) {
        return Pattern.compile(REGEX_VALID_USERNAME)
                .matcher(username)
                .matches();
    }

    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Boolean isValidPassword(String plainPw, String hashedPw) {
        return BCrypt.checkpw(plainPw, hashedPw);
    }

}
