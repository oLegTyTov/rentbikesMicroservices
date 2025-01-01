package com.example.securitymicroservice.exceptions;

public class ExceptionTokenNotFound extends RuntimeException{
public ExceptionTokenNotFound(String token)
{
super(token);
}
}
