package com.spring.jwt.Teachers.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QuestionBankDTO {
    private Integer questionId;
    private String questionText;
    private String subject;
    private String level;

}