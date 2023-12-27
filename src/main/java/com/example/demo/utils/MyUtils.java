package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Log4j2
public final class MyUtils {
    private static final String DATE_FORMAT = "yyyy/MM/dd";

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

    public static LocalDate stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(date, formatter);
    }


    public static boolean isValidDate(String value) {
        Date date;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            date = formatter.parse(value);

            if (!value.equals(formatter.format(date))) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
