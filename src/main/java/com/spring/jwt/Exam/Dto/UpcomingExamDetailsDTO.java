//upcoming exam dto
package com.spring.jwt.Exam.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for Upcoming Exam details")
public class UpcomingExamDetailsDTO {

    @Schema(description = "Title of the exam paper", example = "Math Midterm")
    private String title;

    @Schema(description = "Subject of the exam", example = "Mathematics")
    private String subject;

    @Schema(description = "Scheduled date and time of the exam", example = "2025-09-15T10:00:00")
    private LocalDateTime examDate;

//    @Schema(description = "Class for which the exam is intended", example = "10th Grade")
//    private String studentClass;

    @Schema(description = "Total marks for the exam", example = "100")
    private Integer totalMarks;

}