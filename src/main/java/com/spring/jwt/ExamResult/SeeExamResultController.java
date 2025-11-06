package com.spring.jwt.ExamResult;

import com.spring.jwt.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class SeeExamResultController {

    private final SeeExamResultService seeExamResultService;

    @Operation(
            summary = "Get Student Exam Result",
            description = "Fetches a student's exam result details including subject, topic, exam date, total questions, marks, answered count, not answered count, overall rank, and total students.",
            tags = {"Exam Results"}
    )
    @GetMapping("/studentResult")
    public ResponseEntity<ApiResponse<SeeExamResultDTO>> getStudentExamResult(
            @RequestParam Long userId,
            @RequestParam String subject,
            @RequestParam String topic
    ) {
        SeeExamResultDTO examResult = seeExamResultService.getStudentExamResult(userId, subject, topic);
        return ResponseEntity.ok(ApiResponse.success("Exam result retrieved successfully", examResult));
    }
}
