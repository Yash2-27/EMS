package com.spring.jwt.exception;

public class PersonalInfoResourceNotFoundException extends RuntimeException {
    public PersonalInfoResourceNotFoundException(String message) {
        super(message);
    }
}