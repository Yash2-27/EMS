//upcoming controller
package com.spring.jwt.Exam.controller;

import com.spring.jwt.Exam.Dto.UpcomingExamDetailsDTO;
import com.spring.jwt.Exam.service.UpcomingExamsService;
import com.spring.jwt.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/exam")
public class UpcomingExamController {

    @Autowired
    private UpcomingExamsService upcomingExamsService;

    @Operation(summary = "Get a list of all upcoming exams (After the current time)")
    @GetMapping
    public ResponseEntity<?> getAllUpcomingExams() {
        try {
            List<UpcomingExamDetailsDTO> data = upcomingExamsService.getAllUpcomingExams().getData();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "message", "No upcoming exams found."
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Upcoming exams fetched successfully.",
                    "data", data
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Something went wrong: " + e.getMessage()
            ));
        }
    }

    @Operation(summary = "Get a list of all previous exams (strictly before current time)")
    @GetMapping("/previous")
    public ResponseEntity<?> getAllPreviousExams() {
        try {
            List<UpcomingExamDetailsDTO> data = upcomingExamsService.getAllPreviousExams().getData();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "message", "No previous exams found."
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Previous exams fetched successfully.",
                    "data", data
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Something went wrong: " + e.getMessage()
            ));
        }
    }

    // by student class

//    @Operation(summary = "Get UPCOMING exams by student class (strictly after current time)")
//    @GetMapping({"/upcomingExams/class/", "/upcomingExams/class/{studentClass}"})
//    public ResponseEntity<?> getUpcomingExamsByStudentClass(@PathVariable(required = false) String studentClass) {
//        if (studentClass == null || studentClass.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "message", "No Student Class Selected"
//            ));
//        }
//        try {
//            List<UpcomingExamDetailsDTO> data = upcomingExamsService.getUpcomingExamsByStudentClass(studentClass).getData();
//
//            if (data == null || data.isEmpty()) {
//                return ResponseEntity.status(404).body(Map.of(
//                        "message", "No upcoming exams found for student class: " + studentClass
//                ));
//            }
//
//            return ResponseEntity.ok(Map.of(
//                    "message", "Upcoming exams for class " + studentClass + " fetched successfully.",
//                    "data", data
//            ));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of(
//                    "message", "Something went wrong: " + e.getMessage()
//            ));
//        }
//    }

    @GetMapping("/upcomingExams/class/{studentClass}")
    public ResponseDto<List<UpcomingExamDetailsDTO>> getUpcomingExamsByStudentClass(
            @PathVariable String studentClass) {

        return upcomingExamsService.getUpcomingExamsByStudentClass(studentClass);
    }

    @Operation(summary = "Get previous exams by student class (strictly before current time)")
    @GetMapping("/previousExams/class/{studentClass}")
    public ResponseEntity<?> getPreviousExamsByStudentClass(@PathVariable String studentClass) {
        try {
            if (studentClass == null || studentClass.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Student class is missing or blank."
                ));
            }

            List<UpcomingExamDetailsDTO> data = upcomingExamsService
                    .getPreviousExamsByStudentClass(studentClass)
                    .getData();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "message", "No previous exams found for student class: " + studentClass
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Previous exams for class " + studentClass + " fetched successfully.",
                    "data", data
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Something went wrong: " + e.getMessage()
            ));
        }
    }



    //
//    @GetMapping("/previousExams/class")
//    public ResponseEntity<?> handleMissingStudentClassPrevious() {
//        return ResponseEntity.badRequest().body(Map.of(
//                "message", "studenatClass path variable is required"
//        ));
//    }
//
    @GetMapping("/previousExams/class/")
    public ResponseEntity<?> handleMissingStudentClassPreviousTrailingSlash() {
        return ResponseEntity.badRequest().body(Map.of(
                "message", "No Student class Selected"
        ));
    }


}