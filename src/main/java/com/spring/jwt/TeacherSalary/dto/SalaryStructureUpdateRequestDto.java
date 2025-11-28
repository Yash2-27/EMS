package com.spring.jwt.TeacherSalary.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SalaryStructureUpdateRequestDto {

    @NotNull
    @Positive(message = "Per day salary must be a positive number")
    @Min(value = 500, message = "Per day salary must be at least 500")
    @Max(value = 10000, message = "Per day salary cannot exceed 10000")
    private Integer perDaySalary;

    @NotNull
    @Positive(message = "Annual salary must be a positive number")
    @Min(value = 150000, message = "Annual salary must be at least 100000")
    @Max(value = 3000000, message = "Annual salary cannot exceed 2000000")
    private Integer annualSalary;
}
