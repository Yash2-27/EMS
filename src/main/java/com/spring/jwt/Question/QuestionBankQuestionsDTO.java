package com.spring.jwt.Question;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "All question for question bank ")

public class QuestionBankQuestionsDTO {

    private Integer questionId;
    private String questionText;
//    private String option1;
//    private String option2;
//    private String option3;
//    private String option4;
    private String topic;
    private String level;


}
