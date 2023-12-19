package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;

import java.text.NumberFormat;

@Log4j2
public final class MyUtils {
    public static double stringToNumeric(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return -1.0;
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return false;
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return true;
        }
    }

    public static String formatMoney(String money) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        return formatter.format(stringToNumeric(money));
    }
}
