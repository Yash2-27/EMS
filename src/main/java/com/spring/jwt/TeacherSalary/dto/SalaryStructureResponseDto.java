package com.spring.jwt.TeacherSalary.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SalaryStructureResponseDto {

    private Long structureId;

    @NotNull(message = "Teacher ID cannot be null")
    @Positive(message = "Teacher ID must be a positive number")
    private Integer teacherId;

    @NotBlank(message = "Teacher name cannot be blank")
    private String teacherName;

    @NotNull(message = "Per day salary cannot be null")
    @Positive(message = "Per day salary must be positive")
    @Min(value = 10000 ,message = "Per Day must be minimum 10000")
    @Max(value =10000,message ="Per Day must be maximum 100000" )
    private Integer perDaySalary;

    @NotNull(message = "Annual salary cannot be null")
    @Positive(message = "Annual salary must be positive")
    @Min(value = 100000 ,message = "Annual Salary must be min 100000 ")
    @Max(value = 2000000,message = "Annual Salary must be maximum 2000000")
    private Integer annualSalary;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
