package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log4j2
public final class MyUtils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

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


    public static boolean isValidDate(String date) {
        try {
            LocalDate dateConverted = LocalDate.parse(date, dateFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static LocalDate StringToDate(String date) {
        try {
            return LocalDate.parse(date, dateFormatter);
        } catch (Exception e) {
            return null;
        }
    }
}
