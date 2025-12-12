package com.spring.jwt.TeachersAttendance.controller;
import com.spring.jwt.TeachersAttendance.dto.TeacherAttendanceUpdateDTO;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceResponseDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceSummaryDto;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.service.TeachersAttendanceService;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacherAttendance")
@RequiredArgsConstructor
public class TeachersAttendanceController {

    private final TeachersAttendanceService service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<TeachersAttendanceResponseDto>> createAttendance(
            @Valid @RequestBody TeachersAttendanceDto dto) {

        TeachersAttendanceResponseDto response = service.createAttendance(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Attendance created successfully", response)
        );
    }

    @GetMapping("/teacher")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByTeacherId(
            @RequestParam(required = false) Integer teacherId) {
        List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherId(teacherId);

        return ResponseEntity.ok(ApiResponse.success(
                "Attendance fetched successfully for teacher ID: " + teacherId,
                response
        ));
    }

    @GetMapping("/teacher/month")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByTeacherIdAndMonth(
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) String month) {
        List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherIdAndMonth(teacherId, month);

        return ResponseEntity.ok(ApiResponse.success(
                "Attendance fetched successfully for Teacher ID: " + teacherId + " in month: " + month,
                response
        ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteTeacherAttendance(
            @RequestParam(required = false) Integer attendanceId) {

        if (attendanceId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            HttpStatus.BAD_REQUEST,
                            "Attendance ID cannot be null",
                            "Invalid Attendance ID"
                    ));
        }
        service.deleteTeacherAttendance(attendanceId);
        return ResponseEntity.ok(ApiResponse.success("Attendance deleted successfully", null));
    }

    @GetMapping("/date")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByDate(
            @RequestParam(required = false) String date) {

        List<TeachersAttendanceResponseDto> response = service.getAttendanceByDate(date);

        return ResponseEntity.ok(ApiResponse.success(
                "Attendance fetched successfully for date: " + date,
                response));
    }

    @GetMapping("/teacher/year")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByTeacherIdAndYear(
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) String year) {

        List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherIdAndYear(teacherId, year);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Attendance fetched successfully for teacherId: " + teacherId + " in year: " + year,
                        response
                )
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<TeachersAttendanceSummaryDto>> getMonthlyAttendanceSummary(
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) String month) {

        TeachersAttendanceSummaryDto summary = service.getAttendanceSummaryByTeacherIdAndMonth(teacherId, month);

        return ResponseEntity.ok(ApiResponse.success(
                "Monthly Attendance Summary Fetched Successfully For Teacher ID: " + teacherId,
                summary
        ));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<TeacherAttendanceUpdateDTO>> updateAttendance(
            @RequestParam(required = false) Integer attendanceId,
            @Valid @RequestBody TeachersAttendance updatedAttendance) {

        TeacherAttendanceUpdateDTO updated = service.updateTeacherAttendance(attendanceId, updatedAttendance);

        return ResponseEntity.ok(ApiResponse.success("Attendance updated successfully", updated));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAllAttendance() {

        List<TeachersAttendanceResponseDto> response = service.getAllAttendance();

        if (response == null || response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            HttpStatus.NOT_FOUND,
                            "No attendance found",
                            "Data not available"
                    ));
        }

        return ResponseEntity.ok(ApiResponse.success("Attendance fetched successfully", response));
    }

}

/**
 *
 *
 *
 * Done
 *
 */