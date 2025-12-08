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

    @GetMapping("/getAllStudentClass")
    public ResponseEntity<ApiResponse<List<String>>> getAllClasses() {
        return ResponseEntity.ok(ApiResponse.success("Classes fetched successfully", dropdownService.getAllClasses()));
    }

    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse<List<Object>>> getTeachersByClasses(@RequestParam String studentClass) {
        return ResponseEntity.ok(ApiResponse.success("Teachers fetched successfully", dropdownService.getTeachersByClasses(studentClass)));
    }

    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<String>>> getSubjects(@RequestParam String studentClass,
                                                                 @RequestParam Integer teacherId) {
        return ResponseEntity.ok(ApiResponse.success("Subjects fetched successfully", dropdownService.getSubjects(studentClass, teacherId)));
    }

    @GetMapping("/paper")
    public ResponseEntity<ApiResponse<List<TeacherQuestionFlatDto>>> getQuestionPaper(
            @RequestParam String studentClass,
            @RequestParam Integer teacherId,
            @RequestParam String subject) {

        List<TeacherQuestionFlatDto> papers = dropdownService.getQuestionPaper(studentClass, teacherId, subject);
        return ResponseEntity.ok(ApiResponse.success("Question papers fetched successfully", papers));
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
}
