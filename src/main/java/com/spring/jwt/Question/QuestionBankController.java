package com.spring.jwt.Question;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Operation(summary = "Question Bank - Dropdown all Teachers")
    @GetMapping("/AllTeachers")
    public List<QuestionBankDTO> getTeachersByStudentClass(@RequestParam String studentClass) {
        return questionBankService.getTeachersByStudentClass(studentClass);
    }

    //===============================================================================//
    //                GET SPECIFIC TOPIC WITH RESPECTED TO THERE SUBJECT             //
    //     api/v1/questionBank/topics?subject=Mathematics & StudentClass=11          //
    //===============================================================================//

    @Operation(summary = "Question Bank - Dropdown Topic")
    @GetMapping("/topics")
    public List<QuestionBankSubjectDropdown> getTopicsBySubjectAndStudentClass(
            @RequestParam String subject,
            @RequestParam String studentClass) {
        return questionBankService.getTopicsBySubjectAndStudentClass(subject, studentClass);
    }

    //=========================================================================================================//
    //                                             GET QUESTION BANK                                           //
    //  /api/v1/questionBank/questions?studentClass=12&name=Ms. Neha&subject=Mathematics&topic=Algebra         //
    //=========================================================================================================//
    @Operation(summary = "Question Bank - Fetch all questions")
    @GetMapping("/questions")
    public List<QuestionBankQuestionsDTO> getFilteredQuestions(
            @RequestParam(required = false) String studentClass,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String topic) {

        return questionBankService.getFilteredQuestions(studentClass, name, subject, topic);
    }
}
