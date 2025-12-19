package com.spring.jwt.StudentAttendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentAttendanceSummaryDTO {
    private String studentName;
    private String studentClass;
    private String exam;
    private Long mobileNumber;
    private Double averagePresentPercentage;
}