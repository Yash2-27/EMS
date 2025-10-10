package com.spring.jwt.TeachersAttendance.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TeacherSalaryResponseDto {
    private Integer teacherId;
    private String teacherName;
    private String month;
    private Integer year;
    private Double totalSalary;
    private Double perDaySalary;
    private Map<String, String> attendanceMap; // date -> FULL_DAY/HALF_DAY/ABSENT
}
