package com.spring.jwt.exception.TeacherSalary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TeacherSalaryException extends RuntimeException {

    public TeacherSalaryException(String message) {
        super(message);
    }

    public TeacherSalaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
