package com.spring.jwt.Question;

import com.spring.jwt.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/questionBank")
public class QuestionBankController {

    private final QuestionBankService questionBankService;

    @Autowired
    public QuestionBankController(QuestionBankService questionBankService) {
        this.questionBankService = questionBankService;
    }

    //==========================================================================//
    //                GET ALL TEACHER BY RESPECTED CLASS 11th or 12th           //
    //               /api/v1/questionBank/AllTeachers?studentClass=12           //
    //==========================================================================//
    @GetMapping("/AllTeachers")
    public ResponseEntity<ApiResponse<List<QuestionBankDTO>>> getTeachersByStudentClass(
            @RequestParam String studentClass) {
        List<QuestionBankDTO> teachers = questionBankService.getTeachersByStudentClass(studentClass);
        return ResponseEntity.ok(ApiResponse.success(teachers.toString()));
    }

    //===============================================================================//
    //                GET SPECIFIC TOPIC WITH RESPECTED TO THERE SUBJECT             //
    //     api/v1/questionBank/topics?subject=Mathematics & StudentClass=11          //
    //===============================================================================//



    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<QuestionBankSubjectDropdown>>> getTopicsBySubjectAndStudentClass(
            @RequestParam String subject,
            @RequestParam String studentClass) {
        List<QuestionBankSubjectDropdown> topics = questionBankService.getTopicsBySubjectAndStudentClass(subject, studentClass);
        return ResponseEntity.ok(ApiResponse.success(topics.toString()));
    }

    //=========================================================================================================//
    //                                             GET QUESTION BANK                                           //
    //  /api/v1/questionBank/questions?studentClass=12&name=Ms. Neha&subject=Mathematics&topic=Algebra         //
    //=========================================================================================================//

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<QuestionBankQuestionsDTO>>> getFilteredQuestions(
            @RequestParam(required = false) String studentClass,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String topic) {
        List<QuestionBankQuestionsDTO> questions = questionBankService.getFilteredQuestions(studentClass, name, subject, topic);
        return ResponseEntity.ok(ApiResponse.success(questions.toString()));
    }
}
