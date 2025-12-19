package com.spring.jwt.StudentAttendance.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateStudentAttendanceDTO {
    private LocalDate date;
    private String sub;
    private String name;
    private String mark;
    private Integer teacherId;
    private String studentClass;
    private List<SingleAttendanceDTO> attendanceList;
}