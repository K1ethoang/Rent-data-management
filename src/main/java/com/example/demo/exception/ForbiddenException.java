package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForbiddenException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    private String message = HttpStatus.FORBIDDEN.getReasonPhrase();
}
