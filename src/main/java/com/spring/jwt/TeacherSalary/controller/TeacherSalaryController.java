package com.spring.jwt.TeacherSalary.controller;

import com.spring.jwt.TeacherSalary.dto.AddSalaryDTO;
import com.spring.jwt.TeacherSalary.dto.TeacherSalaryMapper;
import com.spring.jwt.TeacherSalary.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeacherSalary.entity.TeacherSalary;
import com.spring.jwt.TeacherSalary.service.TeacherSalaryService;
import com.spring.jwt.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/teacherSalary")
@RequiredArgsConstructor
public class TeacherSalaryController {

    private final TeacherSalaryService salaryService;


    @PostMapping("/addSalary")
    public ResponseEntity<ApiResponse> addSalary(@Valid @RequestBody AddSalaryDTO dto) {
        TeacherSalary entity = TeacherSalaryMapper.toEntity(dto);
        TeacherSalary saved = salaryService.addTeacherSalary(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Salary added successfully", saved)
        );
    }


    //===================================================================================//
    //                      FOR CALCULATING THE TEACHER SALARY                           //
    //         api/v1/teacherSalary/calculate?teacherId=10000&month=November&year=2025   //
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
