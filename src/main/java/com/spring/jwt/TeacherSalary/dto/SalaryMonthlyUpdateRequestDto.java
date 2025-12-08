package com.spring.jwt.TeacherSalary.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SalaryMonthlyUpdateRequestDto {
    @NotNull(message = "Deduction cant be null")
    @Positive(message = "Deduction  must be a positive number")
    private Integer deduction;
    @NotNull(message = "Status cant be null")
    private String status;
}

