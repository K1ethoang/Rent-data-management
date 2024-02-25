package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;

import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log4j2
public final class MyUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static double stringToDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    public static Integer stringToInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String formatMoney(String money) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        return formatter.format(stringToDouble(money));
    }

    public static LocalDate stringToDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            return LocalDate.parse(date, formatter);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static String getDateNow() {
        LocalDate today = LocalDate.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        return today.format(dateTimeFormatter);
    }

    public static String cleanPath(String path) {
        return path.split("\\.")[0];
    }
}
