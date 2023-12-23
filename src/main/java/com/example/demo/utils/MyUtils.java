package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;

import java.text.NumberFormat;

@Log4j2
public final class MyUtils {
    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static double stringToNumeric(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    public static boolean isNull(String s) {
        return s == null;
    }

    public static boolean isEmpty(String s) {
        return s.isEmpty();
    }


    public static String formatMoney(String money) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        return formatter.format(stringToNumeric(money));
    }
}
