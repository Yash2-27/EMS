package com.spring.jwt.TeacherSalary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSalaryInfoDTO {
    private String teacherName;
    private String studentClass;
    private String date;
    private Double totalSalary;
    private String subject;
}
