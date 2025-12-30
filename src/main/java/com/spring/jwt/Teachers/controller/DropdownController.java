package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.Teachers.service.DropdownService;
import com.spring.jwt.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dropdown")
@RequiredArgsConstructor
public class DropdownController {

    private final DropdownService dropdownService;


    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<String>>> getAllSubjects() {
        return ResponseEntity.ok(ApiResponse.success("Subjects fetched successfully", dropdownService.getAllSubjects()));
    }

    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<List<String>>> getClassesBySubject(@RequestParam String subject) {
        return ResponseEntity.ok(ApiResponse.success("Classes fetched successfully", dropdownService.getClassesBySubject(subject)));
    }

    @GetMapping("/isLiveOptions")
    public ResponseEntity<ApiResponse<List<String>>> getIsLiveOptions(@RequestParam String subject,
                                                                      @RequestParam String studentClass) {
        return ResponseEntity.ok(ApiResponse.success("isLive options fetched successfully", dropdownService.getIsLiveOptions(subject, studentClass)));
    }

    @GetMapping("/paper")
    public ResponseEntity<ApiResponse<List<TeacherQuestionFlatDto>>> getQuestionPaper(@RequestParam String subject,
                                                                                      @RequestParam String studentClass,
                                                                                      @RequestParam String isLive) {
        return ResponseEntity.ok(ApiResponse.success("Question papers fetched successfully",
                dropdownService.getQuestionPaper(subject, studentClass, isLive)));
    }

    @GetMapping("/getAllPaper")
    public ResponseEntity<ApiResponse<List<TeacherQuestionFlatDto>>> getAllQuestionPaper() {
        return ResponseEntity.ok(ApiResponse.success("All Question papers fetched successfully", dropdownService.getAllQuestionPaper()));
    }



//    @GetMapping("/questionBank")
//    public ResponseEntity<?> getQuestionsOnly(
//            @RequestParam String studentClass,
//            @RequestParam Integer teacherId,
//            @RequestParam String subject) {
//
//        return ResponseEntity.ok(dropdownService.getQuestionsOnly(studentClass,subject,teacherId));
//    }

    /**
     @GetMapping("/titles")
     public ResponseEntity<List<String>> getTitles(@RequestParam String studentClass,
     @RequestParam Integer teacherId,
     @RequestParam String subject) {
     List<String> titles = dropdownService.getTitles(studentClass, teacherId, subject);
     return ResponseEntity.ok(titles);
     }
     **/



    @GetMapping("/getAllStudentClass")
    public ResponseEntity<ApiResponse<List<String>>> getAllClasses() {
        return ResponseEntity.ok(ApiResponse.success("Classes fetched successfully", dropdownService.getAllClasses()));
    }

    @GetMapping("/questionBankTeachers")
    public ResponseEntity<ApiResponse<List<Object>>> getTeachersByClasses(@RequestParam String studentClass) {
        return ResponseEntity.ok(ApiResponse.success("Teachers fetched successfully", dropdownService.getTeachersByClasses(studentClass)));
    }

    @GetMapping("/questionBankSubjects")
    public ResponseEntity<ApiResponse<List<String>>> getSubjects(@RequestParam String studentClass,
                                                                 @RequestParam Integer teacherId) {
        return ResponseEntity.ok(ApiResponse.success("Subjects fetched successfully", dropdownService.getSubjects(studentClass, teacherId)));
    }

    @GetMapping("/questionBank")
    public ResponseEntity<ApiResponse<List<TeacherQuestionFlatDto>>> getQuestionBank(
            @RequestParam String studentClass,
            @RequestParam Integer teacherId,
            @RequestParam String subject) {

        List<TeacherQuestionFlatDto> papers = dropdownService.getQuestionBank(studentClass, teacherId, subject);
        return ResponseEntity.ok(ApiResponse.success("Question papers fetched successfully", papers));
    }

    @GetMapping("/getAllQuestionBank")
    public ResponseEntity<ApiResponse<List<TeacherQuestionFlatDto>>> getAllQuestionBank() {
        return ResponseEntity.ok(ApiResponse.success("Question papers fetched successfully", dropdownService.getAllQuestionBank()));
    }

}
