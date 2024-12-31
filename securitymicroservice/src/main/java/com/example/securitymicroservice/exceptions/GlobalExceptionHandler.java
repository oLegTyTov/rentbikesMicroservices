package com.example.securitymicroservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionTokenNotFound.class)
    public ResponseEntity<String> exceptionTokenNotFound(ExceptionTokenNotFound e) {
        return ResponseEntity.badRequest().body("ExceptionTokenNotFound+"+e.getMessage());
    }
    @ExceptionHandler(EmailNotCorrectException.class)
    public ResponseEntity<String> exceptionEmailNotCorrect() {
        return ResponseEntity.badRequest().body("EmailNotCorrectException");
    }
    @ExceptionHandler(MyBadRequestException.class)
    public ResponseEntity<String> myBadRequestException() {
        return ResponseEntity.badRequest().body("MyBadRequestException");
    }
        @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.badRequest().body("ExpiredJwtException");
    }
}
