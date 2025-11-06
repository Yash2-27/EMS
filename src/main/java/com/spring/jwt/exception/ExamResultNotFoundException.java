package com.spring.jwt.exception;

public class ExamResultNotFoundException extends RuntimeException {
    public ExamResultNotFoundException(String message) {
        super(message);
    }
}
