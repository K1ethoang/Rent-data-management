package com.example.demo.helpers;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

public class Helper {
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

    public static void setAllFieldNullToEmpty(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = ReflectionUtils.getField(field, object);

            if (value == null)
                ReflectionUtils.setField(field, object, "");
        }
    }
}
