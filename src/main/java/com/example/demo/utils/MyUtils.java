package com.example.demo.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.List;

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

    // Help to trim all field of object and after setField
    public static void trimToSetAllField(Object object) {
        List<Field> fieldList = List.of(object.getClass().getDeclaredFields());

        for (Field field : fieldList) {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, object);

            if (value == null) continue;

            if (value instanceof String) {
                ReflectionUtils.setField(field, object, ((String) value).trim());
            }
        }
    }
}
