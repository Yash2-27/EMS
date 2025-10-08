package com.spring.jwt.Question;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Teacher details for a specific student class")
public class QuestionBankDTO {

    @Schema(description = "Unique ID of teacher", example = "11")
    private Integer teacherId;

    @Schema(description = "Unique user ID of teacher", example = "101")
    private Integer userId;

    @Schema(description = "Name of the teacher", example = "Ramesh Sharma")
    private String teacherName;

    @Schema(description = "Subject taught by teacher", example = "Chemistry")
    private String subjectName;
}
