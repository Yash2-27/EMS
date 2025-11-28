package com.spring.jwt.TeacherSalary.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SalaryGenerateRequestDto {
    @NotNull(message = "Teacher ID cannot be null")
    @Positive(message = "Teacher ID must be a positive number")
    private Integer teacherId;

    @NotBlank(message = "Month cannot be empty")
    @Pattern(
            regexp = "^(?i)(January|February|March|April|May|June|July|August|September|October|November|December)$",
            message = "Month must be a valid month name"
    )
    private String month;

    @NotNull(message = "Year cannot be null")
    @Positive(message = "Year  must be a positive number")
    @Min(value = 2000, message = "Year must be >= 2000")
    @Max(value = 2100, message = "Year must be <= 2100")
    private Integer year;

}
