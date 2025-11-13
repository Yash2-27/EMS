package com.spring.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AttendanceNotFoundException extends RuntimeException {
    public AttendanceNotFoundException(String message)
    {
        super(message);
    }
}
