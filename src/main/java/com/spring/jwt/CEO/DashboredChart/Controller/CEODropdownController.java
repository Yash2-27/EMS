package com.spring.jwt.CEO.DashboredChart.Controller;

import com.spring.jwt.CEO.DashboredChart.Service.DropdownService;
import com.spring.jwt.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dropdown")
@RequiredArgsConstructor
public class CEODropdownController {
    private final DropdownService dropdownService;

    @GetMapping("/studentClass")
    public ResponseEntity<ApiResponse<List<String>>> getStudentClass() {
        return ResponseEntity.ok(
                ApiResponse.success("Classes fetched successfully", dropdownService.getStudentClass())
        );
    }

    @GetMapping("/studentBatch")
    public ResponseEntity<ApiResponse<List<String>>> getStudentBatch() {
        return ResponseEntity.ok(
                ApiResponse.success("Batches fetched successfully", dropdownService.getStudentBatch())
        );
    }
}
