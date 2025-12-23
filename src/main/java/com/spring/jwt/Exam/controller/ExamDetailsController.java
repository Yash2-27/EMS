package com.spring.jwt.Exam.controller;

import com.spring.jwt.Exam.Dto.UpcomingExamDetailsDTO;
import com.spring.jwt.Exam.service.UpcomingExamsService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamDetailsController {

    @Autowired
    private UpcomingExamsService upcomingExamsService;


    //===========================================================//
    //              Get  All Upcoming Exams                      //
    //                  api/v1/exam                              //
    //===========================================================//
    @Operation(summary = "Get a list of all upcoming exams (After the current time)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getAllUpcomingExams() {
        ResponseDto<List<UpcomingExamDetailsDTO>> response = upcomingExamsService.getAllUpcomingExams();
        return ResponseEntity.ok(ApiResponse.success("Upcoming exams fetched successfully.", response.getData()));
    }

    //===========================================================//
    //                 Get  All Previous Exam                    //
    //                  api/v1/exam/previous                     //
    //===========================================================//
    @Operation(summary = "Get a list of all previous exams (Before current time)")
    @GetMapping("/previous")
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getAllPreviousExams() {
        ResponseDto<List<UpcomingExamDetailsDTO>> response = upcomingExamsService.getAllPreviousExams();
        return ResponseEntity.ok(ApiResponse.success("Previous exams fetched successfully.", response.getData()));
    }

    //===========================================================//
    //                 Get  Upcoming  Exam By Class              //
    //            api/v1/exam/upcomingExams/class/11             //
    //===========================================================//
    @Operation(summary = "Get upcoming exams by student class")
    @GetMapping("/upcomingExams/class/{studentClass}")
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getUpcomingExamsByStudentClass(
            @PathVariable String studentClass) {

        ResponseDto<List<UpcomingExamDetailsDTO>> response =
                upcomingExamsService.getUpcomingExamsByStudentClass(studentClass);

        return ResponseEntity.ok(ApiResponse.success(
                "Upcoming exams for class " + studentClass + " fetched successfully.",
                response.getData()));
    }

    //===========================================================//
    //                 Get  Upcoming  Exam By Class              //
    //            api/v1/exam/previousExam/class/11              //
    //===========================================================//

    @Operation(summary = "Get previous exams by student class")
    @GetMapping("/previousExams/class/{studentClass}")
    public ResponseEntity<ApiResponse<List<UpcomingExamDetailsDTO>>> getPreviousExamsByStudentClass(
            @PathVariable String studentClass) {

        ResponseDto<List<UpcomingExamDetailsDTO>> response =
                upcomingExamsService.getPreviousExamsByStudentClass(studentClass);

        return ResponseEntity.ok(ApiResponse.success(
                "Previous exams for class " + studentClass + " fetched successfully.",
                response.getData()));
    }
}
