package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.service.DropdownService;
import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
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
    public List<String> getAllClasses() {
        return dropdownService.getAllClasses();
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Object>> getTeachersByClasses(@RequestParam String studentClass) {
        List<Object> teachers = dropdownService.getTeachersByClasses(studentClass);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getSubjects(@RequestParam String studentClass,
                                    @RequestParam Integer teacherId) {
        List<String> subjects = dropdownService.getSubjects(studentClass, teacherId);
        return ResponseEntity.ok(subjects);
    }

    /**
    @GetMapping("/titles")
    public List<String> getTitles(@RequestParam String studentClass,
                                  @RequestParam Integer teacherId,
                                  @RequestParam String subject) {
        return dropdownService.getTitles(studentClass,teacherId,subject);
    }
     **/

    @GetMapping("/paper")
    public ResponseEntity<List<TeacherQuestionFlatDto>> getQuestionPaper(
            @RequestParam String studentClass,
            @RequestParam Integer teacherId,
            @RequestParam String subject) {

        List<TeacherQuestionFlatDto> questions = dropdownService.getQuestionPaper(studentClass, teacherId, subject);

        return ResponseEntity.ok(questions);
    }

}
