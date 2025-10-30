package com.spring.jwt.Exam.controller;

import com.spring.jwt.Exam.service.ParentStudentService;
import com.spring.jwt.dto.StudentDTO;
import com.spring.jwt.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class ParentStudentController {

    @Autowired
    private ParentStudentService parentStudentService;


    @Operation(summary = "Provide information on how to use the /students endpoint")
    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> studentsBaseEndpointInfo() {
        String message = "Welcome to the students API endpoint. Please provide a parent ID to retrieve student information, e.g., /students/{parentId}";
        String exampleUsage = "/api/v1/students/parentId/123";

        return ResponseEntity.ok(
                ApiResponse.success(message,
                        java.util.Map.of("example_usage", exampleUsage))
        );
    }


    @GetMapping("/parentId/{parentId}")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getStudentsByParentId(@PathVariable Integer parentId) {
        try {
            List<StudentDTO> students = parentStudentService.getStudentsByParentId(parentId);

            if (students == null || students.isEmpty()) {
                return ResponseEntity.ok(
                        ApiResponse.success("No students found for parent ID: " + parentId)
                );
            }

            return ResponseEntity.ok(
                    ApiResponse.success("Students for parent ID " + parentId + " fetched successfully.", students)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Something went wrong while fetching students",
                            e.getMessage()
                    ));
        }
    }


    @GetMapping("/parentId/")
    public ResponseEntity<ApiResponse<Object>> parentIdMissing() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        HttpStatus.BAD_REQUEST,
                        "Parent ID is missing",
                        "Please provide a valid parent ID in the URL"
                ));
    }
}
