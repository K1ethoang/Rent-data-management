package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;

import java.text.NumberFormat;
import java.util.Locale;

@Log4j2
public final class MyUtils {
    public static double stringToNumeric(String s) {
        if (s.isEmpty())
            s = "0";

        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return -1.0;
        }
    }

    public static boolean isNumeric(String s) {
        int intValue;

        try {
            intValue = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public static String formatCurrency(double money, Locale locale) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(money);
    }
}
