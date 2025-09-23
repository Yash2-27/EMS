package com.spring.jwt.exception;

public class InvalidPersonalInfoException extends RuntimeException {
    public InvalidPersonalInfoException(String message) {
        super(message);
    }
}
