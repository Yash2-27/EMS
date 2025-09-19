package com.spring.jwt.Exam.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectScoreReportDto {
    private String subject;
    private Double averageScore;
}

