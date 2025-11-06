package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import com.spring.jwt.Teachers.service.TeacherService;
import com.spring.jwt.utils.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<TeacherInfoDto>>> getAllTeachers() {
        List<TeacherInfoDto> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(ApiResponse.success("Teachers found", teachers));
    }

    @GetMapping("/getTeachers/{teacherId}")
    public ResponseEntity<ApiResponse<TeacherInfoDto>> getTeacherById(@PathVariable Integer teacherId) {
        TeacherInfoDto teacher = teacherService.findById(teacherId);
        return ResponseEntity.ok(ApiResponse.success("Teacher found", teacher));
    }

}
