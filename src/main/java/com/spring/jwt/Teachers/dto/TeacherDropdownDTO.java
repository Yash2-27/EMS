package com.spring.jwt.Teachers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDropdownDTO {
    private Integer teacherId;
    private String name;
}