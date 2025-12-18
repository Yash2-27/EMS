package com.spring.jwt.StudentAttendance;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class StudentExamDateDTO {
    private String studentName;
    private LocalDate startDate;
    //private String exam;
}
