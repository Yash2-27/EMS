package com.spring.jwt.TeachersAttendance.controller;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceResponseDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceSummaryDto;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.service.TeachersAttendanceService;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.utils.ApiResponse;
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
            @RequestBody TeachersAttendanceDto dto) {
        TeachersAttendanceResponseDto response = service.createAttendance(dto);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            HttpStatus.BAD_REQUEST,
                            "Failed to create attendance",
                            "Invalid input or unable to save record"
                    ));
        }
        return ResponseEntity.ok(ApiResponse.success("Attendance created successfully", response));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByTeacherId(
            @PathVariable Integer teacherId) {
        try {
            List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherId(teacherId);

            return ResponseEntity.ok(ApiResponse.success("Attendance fetched successfully", response));

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            HttpStatus.NOT_FOUND,
                            ex.getMessage(),
                            "No data found"
                    ));
        }
    }

    @PatchMapping("/update/{attendanceId}")
    public ResponseEntity<ApiResponse<TeachersAttendance>> updateTeacherAttendance(
            @PathVariable Integer attendanceId,
            @RequestBody TeachersAttendance updatedAttendance) {

        try {
            TeachersAttendance updated = service.updateTeacherAttendance(attendanceId, updatedAttendance);
            return ResponseEntity.ok(ApiResponse.success("Attendance updated successfully", updated));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage(), "No data found"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid input"));
        }
    }

    @DeleteMapping("/delete/{attendanceId}")
    public ResponseEntity<ApiResponse<String>> deleteTeacherAttendance(@PathVariable Integer attendanceId) {
        try {
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

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            HttpStatus.NOT_FOUND,
                            ex.getMessage(),
                            "No attendance record found with the given ID"
                    ));
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByDate(
            @PathVariable String date) {
        try {
            List<TeachersAttendanceResponseDto> response = service.getAttendanceByDate(date);
            return ResponseEntity.ok(ApiResponse.success("Attendance fetched successfully for date: " + date, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage(), "Invalid date format"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.NOT_FOUND, e.getMessage(), "No attendance found for date"));
        }
    }


    @GetMapping("/month/{month}/{year}")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByMonth(
            @PathVariable String month,
            @PathVariable String year) {
        try {
            List<TeachersAttendanceResponseDto> response = service.getAttendanceByMonth(month, year);
            return ResponseEntity.ok(ApiResponse.success(
                    "Attendance fetched successfully for " + month + "/" + year, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    HttpStatus.BAD_REQUEST, e.getMessage(), "Invalid month/year input"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(
                    HttpStatus.NOT_FOUND, e.getMessage(), "No data found"));
        }
    }

    @GetMapping("/summary/{teacherId}")
    public ResponseEntity<ApiResponse<TeachersAttendanceSummaryDto>> getAttendanceSummaryByTeacherId(
            @PathVariable Integer teacherId) {
        try {
            TeachersAttendanceSummaryDto summary = service.getAttendanceSummaryByTeacherId(teacherId);
            return ResponseEntity.ok(ApiResponse.success("Attendance summary fetched successfully", summary));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    HttpStatus.BAD_REQUEST, e.getMessage(), "Invalid teacher ID"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(
                    HttpStatus.NOT_FOUND, e.getMessage(), "No attendance found"));
        }
    }
}

/**
 *
 *
 *
 */