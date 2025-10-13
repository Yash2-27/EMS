package com.spring.jwt.TeachersAttendance.controller;

import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeachersAttendance.service.TeacherSalaryService;
import com.spring.jwt.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacherSalary")
@RequiredArgsConstructor
public class TeacherSalaryController {

    private final TeacherSalaryService salaryService;

    //===================================================================================//
    //                      FOR CALCULATING THE TEACHER SALARY                           //
    //         api/v1/teacherSalary/calculate?teacherId=10000&month=November &year=2025  //
    //===================================================================================//

    @GetMapping("/calculate")
    public ResponseEntity<ApiResponse<TeacherSalaryResponseDto>> getSalary(
            @RequestParam Integer teacherId,
            @RequestParam String month,
            @RequestParam Integer year) {

        TeacherSalaryResponseDto dto = salaryService.calculateSalary(teacherId, month, year);
        return ResponseEntity.ok(ApiResponse.success("Salary calculated successfully", dto));
    }

    //===========================================================//
    //              FOR ALL TEACHER SALARY INFORMATION           //
    //             /api/v1/teacherSalary/teacherSummary          //
    //===========================================================//


    @GetMapping("/teacherSummary")
    public ResponseEntity<List<TeacherSalaryInfoDTO>> getTeacherSummary() {
        List<TeacherSalaryInfoDTO> list = salaryService.getTeacherSummary();
        return ResponseEntity.ok(list);
    }
}
