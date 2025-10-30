package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import com.spring.jwt.Teachers.service.TeacherService;
import com.spring.jwt.dto.ErrorResponse;
import com.spring.jwt.exception.PapersAndTeacherException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/allTeacher")
    public List<TeacherInfoDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/getTeachers/{teacherId}")
    public ResponseEntity<?> getTeacherById(@PathVariable  Integer teacherId) {
            TeacherInfoDto teacherInfoDto = teacherService.findById(teacherId)
                    .orElseThrow(() -> new PapersAndTeacherException("Teacher not found with id: " + teacherId));
            return ResponseEntity.ok(teacherInfoDto);
    }

    @ExceptionHandler(PapersAndTeacherException.class)
    public ResponseEntity<?> handleException(PapersAndTeacherException e) {
        ErrorResponse PapersAndTeacherNotFound = new ErrorResponse(LocalDateTime.now(), e.getMessage(), "Teacher Not Found");
        return new ResponseEntity<>(PapersAndTeacherNotFound, HttpStatus.NOT_FOUND);
    }

}
