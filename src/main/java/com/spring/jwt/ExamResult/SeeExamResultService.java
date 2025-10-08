package com.spring.jwt.ExamResult;

import java.util.Map;

public interface SeeExamResultService {

    SeeExamResultDTO  getStudentExamResult(Long userId, String subject, String topic);

}
