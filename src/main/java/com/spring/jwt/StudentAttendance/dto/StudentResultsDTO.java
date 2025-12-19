package com.spring.jwt.StudentAttendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StudentResultsDTO {
    private String studentName;
    private String exam;
    private String marks;
    private LocalDate ResultDate;
}
