package com.spring.jwt.ExamResult.exception;

public class ExamResultNotFoundException extends RuntimeException {
    public ExamResultNotFoundException(String message) {
        super(message);
    }
}
