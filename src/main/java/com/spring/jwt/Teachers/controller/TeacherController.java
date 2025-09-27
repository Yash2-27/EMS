package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.service.TeacherService;
import com.spring.jwt.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/allTeacher")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable("id") Integer teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherById(teacherId));
    }
}
