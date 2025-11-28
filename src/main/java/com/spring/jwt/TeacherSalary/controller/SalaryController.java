package com.spring.jwt.TeacherSalary.controller;



import com.spring.jwt.TeacherSalary.dto.*;
import com.spring.jwt.TeacherSalary.service.SalaryService;

import com.spring.jwt.TeacherSalary.service.TeacherSalaryService;
import com.spring.jwt.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacherSalary")
@RequiredArgsConstructor
public class SalaryController {

    private final TeacherSalaryService teacherSalaryService;


    private final SalaryService salaryService;

    //===========================================================//
    //                   CREATE SALARY STRUCTURE                 //
    //            /api/v1/teacherSalary/structure                //
    //===========================================================//
    @Operation(summary = "Create salary structure for a teacher")
    @PostMapping("/structure")
    public ResponseEntity<ApiResponse<SalaryStructureResponseDto>> createStructure(
            @Valid @RequestBody SalaryStructureRequestDto req) {

        return ResponseEntity.ok(
                ApiResponse.success("Salary structure created successfully",
                        salaryService.createStructure(req))
        );
    }


    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<SalaryStructureResponseDto>> patchUpdateStructure(
            @RequestParam Integer teacherId,
            @Valid @RequestBody SalaryStructureUpdateRequestDto req) {

        SalaryStructureResponseDto response = salaryService.updateStructure(teacherId, req);

        return ResponseEntity.ok(
                ApiResponse.success("Salary updated successfully", response)
        );
    }


    @DeleteMapping("/deleteStructure")
    public ResponseEntity<ApiResponse<SalaryStructureResponseDto>> deactivateStructure(
            @RequestParam Integer teacherId) {

        SalaryStructureResponseDto response = salaryService.deactivateStructure(teacherId);

        return ResponseEntity.ok(
                ApiResponse.success("Salary structure deactivated successfully", response)
        );
    }



    // ----------------------------------------------------------
    // FETCH ALL SALARY STRUCTURES
    // ----------------------------------------------------------
    @Operation(summary = "Fetch all salary structures")
    @GetMapping("/structures")
    public ResponseEntity<ApiResponse<List<SalaryStructureResponseDto>>> getAllStructures() {
        List<SalaryStructureResponseDto> structures = salaryService.getAllStructures();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Salary structures fetched successfully",
                        structures
                )
        );
    }



    //===========================================================//
    //                       GENERATE SALARY                     //
    //            /api/v1/teacherSalary/generate                 //
    //===========================================================//
    @Operation(summary = "Generate salary for a teacher")
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<SalaryResponseDto>> generate(
            @Valid @RequestBody SalaryGenerateRequestDto req) {

        return ResponseEntity.ok(
                ApiResponse.success("Salary generated successfully",
                        salaryService.generateSalary(req))
        );
    }

    //===========================================================//
    //                       UPDATE DEDUCTION                    //
    //           /api/v1/teacherSalary/deduction/{id}            //
    //===========================================================//
    @Operation(summary = "Update salary deduction for a teacher")
    @PatchMapping("/deduction/{id}")
    public ResponseEntity<ApiResponse<SalaryResponseDto>> updateDeduction(
            @PathVariable Long id,
            @RequestParam Integer deduction) {

        return ResponseEntity.ok(
                ApiResponse.success("Deduction updated successfully",
                        salaryService.updateDeduction(id, deduction))
        );
    }

    //===========================================================//
    //                          PAY SALARY                       //
    //               /api/v1/teacherSalary/pay/{id}              //
    //===========================================================//
    @Operation(summary = "Mark salary as paid for a teacher")
    @PostMapping("/pay/{id}")
    public ResponseEntity<ApiResponse<SalaryResponseDto>> paySalary(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Salary paid successfully",
                        salaryService.paySalary(id))
        );
    }

    //===========================================================//
    //                       SALARY HISTORY                      //
    //               /api/v1/teacherSalary/history               //
    //===========================================================//
    @Operation(summary = "Get complete salary payment history of a teacher")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<SalaryResponseDto>>> history(
            @RequestParam Integer teacherId) {

        return ResponseEntity.ok(
                ApiResponse.success("Salary history fetched",
                        salaryService.getSalaryHistory(teacherId))
        );
    }

    //===========================================================//
    //                     MONTHLY SALARY INFO                   //
    //               /api/v1/teacherSalary/monthly               //
    //===========================================================//
    @Operation(summary = "Get monthly salary details of a teacher")
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<SalaryResponseDto>> monthly(
            @RequestParam Integer teacherId,
            @RequestParam String month,
            @RequestParam Integer year) {

        return ResponseEntity.ok(
                ApiResponse.success("Monthly salary fetched",
                        salaryService.getSalaryByMonth(teacherId, month, year))
        );
    }

    //===========================================================//
    //              FOR ALL TEACHER SALARY INFORMATION           //
    //             /api/v1/teacherSalary/teacherSummary          //
    //===========================================================//
    @Operation(summary = "Get summary of all teachers' salaries")
    @GetMapping("/teacherSummary")
    public ResponseEntity<ApiResponse<List<TeacherSalaryInfoDTO>>> getTeacherSummary() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Teacher summary fetched",
                        teacherSalaryService.getTeacherSummary()  // <-- normal method call
                )
        );
    }

}

