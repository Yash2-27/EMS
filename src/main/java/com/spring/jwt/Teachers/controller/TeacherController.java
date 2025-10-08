package com.spring.jwt.Teachers.controller;

import com.spring.jwt.Teachers.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    /**
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
    **/

}
