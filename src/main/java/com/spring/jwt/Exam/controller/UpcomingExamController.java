package com.spring.jwt.Exam.controller;

import com.spring.jwt.Exam.Dto.UpcomingExamDetailsDTO;
import com.spring.jwt.Exam.service.UpcomingExamsService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UpcomingExamController {

    @Autowired
    private UpcomingExamsService upcomingExamsService;



    @Operation(summary = "Get a list of all upcoming exams (After the current time)")
    @GetMapping
    public ResponseEntity<ResponseDto<List<UpcomingExamDetailsDTO>>> getAllUpcomingExams() {
        try {
            ResponseDto<List<UpcomingExamDetailsDTO>> response = upcomingExamsService.getAllUpcomingExams();
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ResponseDto.error("Not found", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseDto.error("Failed to retrieve upcoming exams", e.getMessage()));
        }
    }


    @Operation(summary = "Get a list of all previous exams (strictly before current time)")
    @GetMapping("/previous")
    public ResponseEntity<ResponseDto<List<UpcomingExamDetailsDTO>>> getAllPreviousExams() {
        try {
            ResponseDto<List<UpcomingExamDetailsDTO>> response = upcomingExamsService.getAllPreviousExams();
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) { //
            return ResponseEntity.status(404).body(ResponseDto.error("Not found", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseDto.error("Failed to retrieve previous exams for this class", e.getMessage()));
        }
    }

    //API for PARENTS //Get details of upcoming exams.
    @Operation(summary = "Get UPCOMING exams by student class (strictly after current time)")
    @GetMapping("/upcomingexams/class/{studentClass}")
    public ResponseEntity<ResponseDto<List<UpcomingExamDetailsDTO>>> getUpcomingExamsByStudentClass(@PathVariable String studentClass) {
        try {
            ResponseDto<List<UpcomingExamDetailsDTO>> response = upcomingExamsService.getUpcomingExamsByStudentClass(studentClass);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ResponseDto.error("Not found", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseDto.error("Failed to retrieve upcoming exams for student  class", e.getMessage()));
        }
    }


    //API for PARENTS //Get details of previous  exams
    @Operation(summary = "Get previous exams by student class (strictly before current time)")
    @GetMapping("/previousexams/class/{studentClass}")
    public ResponseEntity<ResponseDto<List<UpcomingExamDetailsDTO>>> getPreviousExamsByStudentClass(@PathVariable String studentClass) {
        try {
            ResponseDto<List<UpcomingExamDetailsDTO>> response = upcomingExamsService.getPreviousExamsByStudentClass(studentClass);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) { // Directly catch the exception
            return ResponseEntity.status(404).body(ResponseDto.error("Not found", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseDto.error("Failed to retrieve previous exams for class", e.getMessage()));
        }
    }

//
//    @Operation(summary = "Get a specific upcoming exam by its paper ID")
//    @GetMapping("/{paperId}")
//    public ResponseEntity<ResponseDto<UpcomingExamDetailsDTO>> getUpcomingExamDetailsById(@PathVariable Integer paperId) {
//        try {
//            ResponseDto<UpcomingExamDetailsDTO> response = upcomingExamsService.getUpcomingExamDetailsById(paperId);
//            if (response.getData() == null) {
//                return ResponseEntity.status(404).body(response);
//            }
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(
//                    ResponseDto.error("Failed to retrieve upcoming exam by Paper ID", e.getMessage()));
//        }
//    }
//
//    @Operation(summary = "Get a specific upcoming exam by its UpcomingExam ID")
//    @GetMapping("/details/{upcomingExamId}")
//    public ResponseEntity<ResponseDto<UpcomingExamDetailsDTO>> getUpcomingExamDetailsByUpcomingExamId(@PathVariable Integer upcomingExamId) {
//        try {
//            ResponseDto<UpcomingExamDetailsDTO> response = upcomingExamsService.getUpcomingExamDetailsByUpcomingExamId(upcomingExamId);
//            if (response.getData() == null) {
//                return ResponseEntity.status(404).body(response);
//            }
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(
//                    ResponseDto.error("Failed to retrieve upcoming exam by UpcomingExam ID", e.getMessage()));
//        }
//    }


}