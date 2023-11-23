package com.example.demo.exception;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Exception hệ thống
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error");
    }

    // Exception ứng dụng
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException e) {
        log.trace(e.getMessage());
        return ResponseEntity.status(e.getCode()).body(e.getMessage());
    }
}
