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


    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByTeacherId(
            @PathVariable Integer teacherId) {

        List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherId(teacherId);

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


    @GetMapping("/teacher/{teacherId}/month/{month}")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByTeacherIdAndMonth(
            @PathVariable Integer teacherId,
            @PathVariable String month) {

        List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherIdAndMonth(teacherId, month);

        if (response == null || response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            HttpStatus.NOT_FOUND,
                            "No attendance found for Teacher ID: " + teacherId + " in month: " + month,
                            "No data found"
                    ));
        }

        return ResponseEntity.ok(ApiResponse.success(
                "Attendance fetched successfully for Teacher ID: " + teacherId + " in month: " + month,
                response));
    }

    @DeleteMapping("/delete/{attendanceId}")
    public ResponseEntity<ApiResponse<String>> deleteTeacherAttendance(@PathVariable Integer attendanceId) {
        try {
            if (attendanceId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(
                                HttpStatus.BAD_REQUEST,
                                "Attendance ID cannot be null",
                                "Invali d Attendance ID"
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

        List<TeachersAttendanceResponseDto> response = service.getAttendanceByDate(date);

        if (response == null || response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            HttpStatus.NOT_FOUND,
                            "No attendance found for date: " + date,
                            "No data found"
                    ));
        }
        return ResponseEntity.ok(ApiResponse.success(
                "Attendance fetched successfully for date: " + date,
                response));
    }

    @GetMapping("teacherId/{teacherId}/year/{year}")
    public ResponseEntity<ApiResponse<List<TeachersAttendanceResponseDto>>> getAttendanceByTeacherIdAndYear(
            @PathVariable Integer teacherId,
            @PathVariable String year) {

        List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherIdAndYear(teacherId, year);

        if (response == null || response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            HttpStatus.NOT_FOUND,
                            "No attendance found for teacherId: " + teacherId + " in year: " + year,
                            "No data found"
                    ));
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Attendance fetched successfully for teacherId: " + teacherId + " in year: " + year,
                        response
                )
        );
    }
    @GetMapping("/summary/{teacherId}/month/{month}")
    public ResponseEntity<ApiResponse<TeachersAttendanceSummaryDto>> getMonthlyAttendanceSummary(
            @PathVariable Integer teacherId,
            @PathVariable String month) {

        TeachersAttendanceSummaryDto summary = service.getAttendanceSummaryByTeacherIdAndMonth(teacherId, month);

        if (summary == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(
                            HttpStatus.NOT_FOUND,
                            "No attendance data found for Teacher ID: " + teacherId + " in month: " + month,
                            "No data found"
                    ));
        }

        return ResponseEntity.ok(ApiResponse.success(
                "Monthly Attendance Summary Fetched Successfully For Teacher ID: " + teacherId,
                summary));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TeacherAttendanceUpdateDTO>> updateAttendance(
            @PathVariable("id") Integer attendanceId,
            @Valid @RequestBody TeachersAttendance updatedAttendance) {

        TeacherAttendanceUpdateDTO updated = service.updateTeacherAttendance(attendanceId, updatedAttendance);

        TeacherAttendanceUpdateDTO response = new TeacherAttendanceUpdateDTO();
        response.setAttendanceId(updated.getAttendanceId());
//        response.setTeacherId(updated.getTeacherId());
        response.setTeacherName(updated.getTeacherName());
        response.setMonth(updated.getMonth());
        response.setDate(updated.getDate());
        response.setInTime(updated.getInTime());
        response.setOutTime(updated.getOutTime());
        response.setMark(updated.getMark());

        return ResponseEntity.ok(ApiResponse.success("Attendance updated successfully", response));
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