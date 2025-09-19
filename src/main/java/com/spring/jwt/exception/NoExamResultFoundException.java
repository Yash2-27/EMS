package com.spring.jwt.exception;

public class NoExamResultFoundException extends RuntimeException {
    public NoExamResultFoundException(String message) {
        super(message);
    }
}
