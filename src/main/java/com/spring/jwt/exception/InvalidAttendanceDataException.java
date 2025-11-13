package com.spring.jwt.exception;

public class InvalidAttendanceDataException extends RuntimeException {
    public InvalidAttendanceDataException(String message) {

        super(message);
    }
}
