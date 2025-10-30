package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.Teachers.exception.DropdownResourceNotFoundException;
import com.spring.jwt.Teachers.service.DropdownService;
import com.spring.jwt.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dropdown")
@RequiredArgsConstructor
public class DropdownController {

    private final DropdownService dropdownService;

    @GetMapping("/getAllStudentClass")
    public ResponseEntity<List<String>> getAllClasses() {
        return ResponseEntity.ok(dropdownService.getAllClasses());
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Object>> getTeachersByClasses(@RequestParam String studentClass) {
        return ResponseEntity.ok(dropdownService.getTeachersByClasses(studentClass));
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getSubjects(@RequestParam String studentClass,
                                                    @RequestParam Integer teacherId) {
        return ResponseEntity.ok(dropdownService.getSubjects(studentClass, teacherId));
    }

    @GetMapping("/paper")
    public ResponseEntity<List<TeacherQuestionFlatDto>> getQuestionPaper(
            @RequestParam String studentClass,
            @RequestParam Integer teacherId,
            @RequestParam String subject) {
        return ResponseEntity.ok(dropdownService.getQuestionPaper(studentClass, teacherId, subject));
    }

    /**
     @GetMapping("/titles")
     public ResponseEntity<List<String>> getTitles(@RequestParam String studentClass,
     @RequestParam Integer teacherId,
     @RequestParam String subject) {
     List<String> titles = dropdownService.getTitles(studentClass, teacherId, subject);
     return ResponseEntity.ok(titles);
     }
     **/

    @ExceptionHandler(DropdownResourceNotFoundException.class)
    public ResponseEntity<?> handleException(DropdownResourceNotFoundException e) {
        ErrorResponse DropdownControllerNotFound = new ErrorResponse(LocalDateTime.now(), e.getMessage(), "Dropdown Resource Not Found");
        return new ResponseEntity<>(DropdownControllerNotFound, HttpStatus.NOT_FOUND);
    }
}