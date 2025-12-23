package com.spring.jwt.StudentAttendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDropdownDTO {
    private Long userId;
    private String studentName;
}
