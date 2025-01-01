package com.example.bikemicroservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FilterException.class)
    public ResponseEntity<String> exceptionTokenNotFound(FilterException e) {
        return ResponseEntity.badRequest().body("FilterException");
    }
    
}
