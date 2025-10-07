package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.exception.PapersAndTeacherException;
import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import com.spring.jwt.Teachers.service.TeacherService;
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
    public List<TeacherInfoDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/getTeachers/{teacherId}")
    public ResponseEntity<?> getTeacherById(@PathVariable  Integer teacherId) {
            TeacherInfoDto teacherInfoDto = teacherService.findById(teacherId)
                    .orElseThrow(() -> new PapersAndTeacherException("Teacher not found with id: " + teacherId));
            return ResponseEntity.ok(teacherInfoDto);
    }

}
