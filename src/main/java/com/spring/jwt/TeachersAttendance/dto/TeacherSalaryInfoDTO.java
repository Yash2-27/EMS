package com.spring.jwt.TeachersAttendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSalaryInfoDTO {
    private Integer serialNo;      // Auto increment
    private String teacherName;
    private String studentClass;
    private String date;           // created_at
    private Double totalSalary;
    private String subject;


}
