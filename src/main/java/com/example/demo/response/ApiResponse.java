package com.example.demo.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
    public static ResponseEntity<Object> responseBuilder(HttpStatus httpStatus, String message, Object responseObject) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", httpStatus.value());
        response.put("message", message);
        response.put("data", responseObject);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<Object> responseBuilder(HttpStatus httpStatus, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", httpStatus.value());
        response.put("message", message);

        return new ResponseEntity<>(response, httpStatus);
    }
}
