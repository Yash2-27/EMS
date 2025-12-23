package com.spring.jwt.StudentAttendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentExamResultDTO {
    private String candidateName;
    private String className;
    private String examName;
    private Long rollNumber;
    private String subjectName;

    private Double score;
    private Double totalMarks;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Integer unattemptedQuestions;
}
