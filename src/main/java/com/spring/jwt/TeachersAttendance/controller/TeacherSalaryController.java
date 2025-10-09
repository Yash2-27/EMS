package com.spring.jwt.TeachersAttendance.controller;

import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeachersAttendance.service.TeacherSalaryService;
import com.spring.jwt.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teacherSalary")
@RequiredArgsConstructor
public class TeacherSalaryController {

    private final TeacherSalaryService salaryService;

    // GET salary by month/year
    @GetMapping("/calculate/{teacherId}/{month}/{year}")
    public ResponseEntity<ApiResponse<TeacherSalaryResponseDto>> getSalary(
            @PathVariable Integer teacherId,
            @PathVariable String month,
            @PathVariable Integer year) {

        TeacherSalaryResponseDto dto = salaryService.calculateSalary(teacherId, month, year);
        return ResponseEntity.ok(ApiResponse.success("Salary calculated successfully", dto));
    }
}
