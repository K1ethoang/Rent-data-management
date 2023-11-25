package com.example.demo.exception;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Exception ứng dụng
    @ExceptionHandler(AppException.class)
    public ErrorMessage handlerAppException(AppException e) {
        log.trace(e.getMessage());
        return new ErrorMessage(e.getCode().value(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handlerNotFoundException(NotFoundException e) {
        log.trace(e.getMessage());
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    // Exception hệ thống
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handlerException(Exception e) {
        log.error(e.getMessage());
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown error");
    }

}
