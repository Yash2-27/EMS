package com.spring.jwt.StudentAttendance;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StudentResultsDTO {
    private String studentName;
    private LocalDate ResultDate;
    private String marks;
    //private String exam;
}
