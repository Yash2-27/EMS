package com.spring.jwt.ExamResult;

import com.spring.jwt.utils.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Exam result retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SeeExamResultDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Exam result not found (user, subject, or topic missing)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/studentResult")
    public ResponseEntity<SeeExamResultDTO> getStudentExamResult(
            @RequestParam Long userId,
            @RequestParam String subject,
            @RequestParam String topic
    ) {
        SeeExamResultDTO examResult = seeExamResultService.getStudentExamResult(userId, subject, topic);
        return ResponseEntity.ok(examResult);
    }
}
