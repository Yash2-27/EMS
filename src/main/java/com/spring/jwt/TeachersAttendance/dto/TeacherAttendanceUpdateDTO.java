package com.spring.jwt.TeachersAttendance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeacherAttendanceUpdateDTO {

    private Integer attendanceId;

    private Integer teacherId;
    private String teacherName;
    private String date;
    private String month;
    private String inTime;
    private String outTime;
    private String mark;

}
