package com.spring.jwt.Question;

import java.util.List;

public interface QuestionBankService {

    List<QuestionBankDTO> getTeachersByStudentClass(String studentClass);

    List<QuestionBankSubjectDropdown> getTopicsBySubjectAndStudentClass(String subject, String studentClass);


        List<QuestionBankQuestionsDTO> getFilteredQuestions(String studentClass,
                                                            String name,
                                                            String subject,
                                                            String topic);

}
