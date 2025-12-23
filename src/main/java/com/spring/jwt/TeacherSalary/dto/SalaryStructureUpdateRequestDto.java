package com.spring.jwt.TeacherSalary.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SalaryStructureUpdateRequestDto {

    @Positive(message = "Per day salary must be a positive number")
    @Min(value = 500, message = "Per day salary must be at least 500")
    @Max(value = 10000, message = "Per day salary cannot exceed 10000")
    private Integer perDaySalary;

    @Positive(message = "Annual salary must be a positive number")
    @Min(value = 100000, message = "Annual salary must be at least 100000")
    @Max(value = 3000000, message = "Annual salary cannot exceed 3000000")
    private Integer annualSalary;

    // Optional field
    private String status;
}
