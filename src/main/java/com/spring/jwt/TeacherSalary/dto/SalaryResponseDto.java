package com.spring.jwt.TeacherSalary.dto;

import lombok.Data;

@Data
public class SalaryResponseDto {
    private Long salaryId;
    private Integer teacherId;
    private String teacherName;
    private String month;
    private Integer year;
    private Double calculatedSalary;
    private Integer deduction;
    private Double finalSalary;
    private String status;
    private String paymentDate;
    private Integer presentDays;
    private Integer absentDays;
    private Integer HalfDays;
    private Integer lateDays;
    private Integer perDaySalary;
    private Integer totalDays;

}

