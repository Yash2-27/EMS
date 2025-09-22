//parentcontroller

package com.spring.jwt.Exam.controller;

import com.spring.jwt.Exam.service.ParentStudentService;
import com.spring.jwt.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/students")
public class ParentStudentController {

    @Autowired
    private ParentStudentService parentStudentService;

    @Operation(summary = "Provide information on how to use the /students endpoint")
    @GetMapping("")
    public ResponseEntity<?> studentsBaseEndpointInfo() {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to the students API endpoint. Please provide a parent ID to retrieve student information, e.g., /students/{parentId}",
                "example_usage", "/students/123"
        ));
    }

    @Operation(summary = "Get students associated with a specific parent ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students fetched successfully or no students found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/parentId/{parentId}")
    public ResponseEntity<?> getStudentsByParentId(@PathVariable Integer parentId) {
        try {
            List<StudentDTO> students = parentStudentService.getStudentsByParentId(parentId);

            if (students == null || students.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "message", "No students found for parent ID: " + parentId
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Students for parent ID " + parentId + " fetched successfully.",
                    "data", students
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Something went wrong while fetching students: " + e.getMessage()
            ));

        }

    }   @GetMapping("/parentId/")
    public ResponseEntity<?> parentIdMissing() {
        return ResponseEntity.badRequest().body(Map.of(
                "message", "Parent ID is missing"
        ));
    }

}
