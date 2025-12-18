package com.spring.jwt.TeacherSalary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherMonthlyDropdown {
    private Integer teacherId;
    private String teacherName;
    private String month;
    private Integer year;


}
