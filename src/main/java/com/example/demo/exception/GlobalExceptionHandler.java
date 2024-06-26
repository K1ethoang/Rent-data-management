package com.example.demo.exception;


import com.example.demo.response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handlerNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
    }

    @ExceptionHandler(InValidException.class)
    public ErrorResponse handlerNotValidException(InValidException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponse handlerForbiddenException(ForbiddenException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
    }

    @ExceptionHandler(NotNullException.class)
    public ErrorResponse handlerNotNullException(NotNullException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
    }

    @ExceptionHandler(NoContentException.class)
    public ErrorResponse handlerNoContentException(NoContentException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
    }

    @ExceptionHandler(DuplicatedException.class)
    public ErrorResponse handlerDuplicatedException(DuplicatedException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handlerAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    // Exception hệ thống
    @ExceptionHandler(Exception.class)
    public ErrorResponse handlerException(Exception e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
