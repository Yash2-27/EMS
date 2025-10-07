package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.Teachers.service.DropdownService;
import lombok.RequiredArgsConstructor;
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
    public List<Object> getTeachersByClasses(@RequestParam String studentClass) {
        return dropdownService.getTeachersByClasses(studentClass);
    }


    @GetMapping("/subjects")
    public List<String> getSubjects(@RequestParam String studentClass,
                                    @RequestParam Integer teacherId) {
        return dropdownService.getSubjects(studentClass,teacherId);
    }

    @GetMapping("/titles")
    public List<String> getTitles(@RequestParam String studentClass,
                                  @RequestParam Integer teacherId,
                                  @RequestParam String subject) {
        return dropdownService.getTitles(studentClass,teacherId,subject);
    }

    @GetMapping("/paper")
    public List<TeacherQuestionFlatDto> getQuestionPaper(@RequestParam String studentClass,
                                                         @RequestParam Integer teacherId,
                                                         @RequestParam String subject,
                                                         @RequestParam String title) {
        return dropdownService.getQuestionPaper(studentClass, teacherId, subject, title);
    }


}
