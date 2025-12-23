package com.spring.jwt.TeacherSalary.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SalaryStructureRequestDto {
    @Positive(message = "Teacher ID must be a positive number")
    @NotNull(message = "Teacher ID is required")
    private Integer teacherId;

    @NotNull(message = "Per day salary is required")
    @Positive(message = "Per day salary must be a positive number")
    @Min(value = 500, message = "Per day salary must be greater than 500")
    @Max(value=10000 ,message = "Per day salary must be under 10000")
    private Integer perDaySalary;

    @NotNull(message = "Annual salary is required")
    @Positive(message = "Annual  salary must be a positive number")
    @Min(value = 100000, message = "Annual salary must be greater than 100000")
    @Max(value=3000000, message = "maximum Limit is reached cant extend after 3000000 ")
    private Integer annualSalary;

}
