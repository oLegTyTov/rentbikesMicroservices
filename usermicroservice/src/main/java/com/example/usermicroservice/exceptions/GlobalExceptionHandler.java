package com.example.usermicroservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.usermicroservice.dtos.ResponseCheck;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserEmailException.class)
    public ResponseEntity<String> handleCreateUserEmailException() {
        return ResponseEntity.badRequest().body("errorCreateUserEmailException");
    }
    
    @ExceptionHandler(UserUsernameException.class)
    public ResponseEntity<String> handleCreateUserUsernameException() {
        return ResponseEntity.badRequest().body("errorCreateUserUsernameException");
    }
    
    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<Boolean> handleEmailNotValidException() {
        return ResponseEntity.badRequest().body(Boolean.FALSE);
    }
    
    @ExceptionHandler(PasswordNotCorrectException.class)
    public ResponseEntity<Boolean> handlePasswordNotCorrectException() {
        return ResponseEntity.badRequest().body(Boolean.FALSE);
    }
    
}
