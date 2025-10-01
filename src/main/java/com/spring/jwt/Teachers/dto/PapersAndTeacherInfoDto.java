package com.spring.jwt.Teachers.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PapersAndTeacherInfoDto {
    private String teacherName;
    private String subject;
    private String topic;
    private String studentClass;
    // private String date;
}
