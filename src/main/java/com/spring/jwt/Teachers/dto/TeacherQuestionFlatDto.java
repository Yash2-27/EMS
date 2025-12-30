package com.spring.jwt.Teachers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherQuestionFlatDto {
    private String title;
    private String questionText;
    private String options1;
    private String options2;
    private String options3;
    private String options4;
    private Boolean isLive;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
