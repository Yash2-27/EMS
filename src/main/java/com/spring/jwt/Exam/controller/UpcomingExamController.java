package com.spring.jwt.Exam.controller;

import com.spring.jwt.Exam.Dto.UpcomingExamDetailsDTO;
import com.spring.jwt.Exam.service.UpcomingExamsService;
import com.spring.jwt.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam")
public class UpcomingExamController {

    @Autowired
    private UpcomingExamsService upcomingExamsService;

    @Operation(summary = "Get a list of all upcoming exams (After the current time)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getAllUpcomingExams() {
        try {
            List<UpcomingExamDetailsDTO> data = upcomingExamsService.getAllUpcomingExams().getData();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND, "No upcoming exams found.", null));
            }

            return ResponseEntity.ok(ApiResponse.success("Upcoming exams fetched successfully.", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Something went wrong while fetching upcoming exams.",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Get a list of all previous exams (strictly before current time)")
    @GetMapping("/previous")
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getAllPreviousExams() {
        try {
            List<UpcomingExamDetailsDTO> data = upcomingExamsService.getAllPreviousExams().getData();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND, "No previous exams found.", null));
            }

            return ResponseEntity.ok(ApiResponse.success("Previous exams fetched successfully.", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Something went wrong while fetching previous exams.",
                            e.getMessage()));
        }
    }

    @Operation(summary = "Get upcoming exams by student class (strictly after current time)")
    @GetMapping("/upcomingExams/class/{studentClass}")
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getUpcomingExamsByStudentClass(
            @PathVariable String studentClass) {
        try {
            if (studentClass == null || studentClass.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Student class is missing.", null));
            }

            List<UpcomingExamDetailsDTO> data =
                    upcomingExamsService.getUpcomingExamsByStudentClass(studentClass).getData();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND,
                                "No upcoming exams found for student class: " + studentClass, null));
            }

            return ResponseEntity.ok(ApiResponse.success(
                    "Upcoming exams for class " + studentClass + " fetched successfully.", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Something went wrong while fetching upcoming exams by class.",
                            e.getMessage()));
        }
    }

//    @GetMapping("/upcomingExams/class/")
//    public ResponseEntity<ApiResponse<?>> handleMissingStudentClassUpcoming() {
//        return ResponseEntity.badRequest()
//                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "No student class selected.", null));
//    }

    @Operation(summary = "Get previous exams by student class (strictly before current time)")
    @GetMapping("/previousExams/class/{studentClass}")
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getPreviousExamsByStudentClass(
            @PathVariable String studentClass) {
        try {
            if (studentClass == null || studentClass.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Student class is missing.", null));
            }

            List<UpcomingExamDetailsDTO> data =
                    upcomingExamsService.getPreviousExamsByStudentClass(studentClass).getData();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(HttpStatus.NOT_FOUND,
                                "No previous exams found for student class: " + studentClass, null));
            }

            return ResponseEntity.ok(ApiResponse.success(
                    "Previous exams for class " + studentClass + " fetched successfully.", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Something went wrong while fetching previous exams by class.",
                            e.getMessage()));
        }
    }

//    @GetMapping("/previousExams/class/")
//    public ResponseEntity<ApiResponse<?>> handleMissingStudentClassPrevious() {
//        return ResponseEntity.badRequest()
//                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "No student class selected.", null));
//    }
}
