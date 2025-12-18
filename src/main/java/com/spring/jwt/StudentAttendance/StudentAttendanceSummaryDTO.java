package com.spring.jwt.StudentAttendance;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAttendanceSummaryDTO {
    private String studentName;
    //private String exam;
    private String studentClass;
    private Long mobileNumber;
    private Double averagePresentPercentage;
}