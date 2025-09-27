package com.spring.jwt.ExamResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeeExamResultDTO {
    private String subject;
    private String topic;
    private String examDate;
    private Integer noOfQuestions;
    private Integer answered;
    private Integer notAnswered;
    private Integer overallRank;
    private Integer totalStudents;
    private Integer studentMarks;
    private Integer totalMarks;
}
