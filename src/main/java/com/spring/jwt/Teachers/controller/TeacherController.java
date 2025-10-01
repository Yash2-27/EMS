package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.dto.PapersAndTeacherInfoDto;
import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import com.spring.jwt.Teachers.service.TeacherService;
import com.spring.jwt.dto.TeacherDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/allTeacher")
    public List<TeacherInfoDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{teacherId}")
    @Operation(summary = "Get teacher by ID", description = "Fetches a specific teacher using their ID")
    public TeacherInfoDto getTeacherById(@PathVariable Integer teacherId) {
        return teacherService.getTeacherById(teacherId);
    }

    @GetMapping("{teacherId}/papers")
    public ResponseEntity<?> getPapersByTeacherId(@PathVariable Integer teacherId) {
        List<PapersAndTeacherInfoDto> papers = teacherService.getPapersByTeacherId(teacherId);
        if (papers.isEmpty()) {
            return ResponseEntity.ok("No papers found for teacher ID:" + teacherId);
        }
        return ResponseEntity.ok(papers);
    }

}
