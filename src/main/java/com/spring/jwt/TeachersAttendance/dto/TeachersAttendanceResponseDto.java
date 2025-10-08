package com.spring.jwt.TeachersAttendance.dto;

import lombok.Data;

@Data
public class TeachersAttendanceResponseDto {
    private Integer attendanceId;
    private Integer teacherId;
    private String teacherName;
    private String date;
    private String month;
    private String inTime;
    private String outTime;
    private String mark;
}
