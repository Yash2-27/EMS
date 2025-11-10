package com.spring.jwt.TeachersAttendance.dto;

import lombok.Data;

@Data
public class TeachersAttendanceSummaryDto {
    private Integer teacherId;
    private String teacherName;
    private long totalDays;
    private long fullDays;
    private long halfDays;
    private long absentDays;
    private String month;
    private double attendancePercentage;
}
