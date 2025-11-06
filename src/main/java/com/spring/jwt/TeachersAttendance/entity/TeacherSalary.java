package com.spring.jwt.TeachersAttendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "teacher_salary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    @NotNull(message = "Teacher ID cannot be null")
    @Positive(message = "Teacher ID must be a positive number")
    private Long teacherId;

    @NotBlank(message = "Month cannot be empty")
    @Pattern(
            regexp = "^(?i)(January|February|March|April|May|June|July|August|September|October|November|December)$",
            message = "Month must be a valid month name (e.g., January, February, etc.)"
    )
    private String month;

    @NotNull(message = "Year cannot be null")
    @Min(value = 2000, message = "Year must be greater than or equal to 2000")
    @Max(value = 2100, message = "Year must be less than or equal to 2100")
    private Integer year;

    @NotNull(message = "Per day salary cannot be null")
    @Positive(message = "Per day salary must be positive")
    private Double perDaySalary;

    @NotNull(message = "Salary cannot be null")
    @PositiveOrZero(message = "Salary must be zero or positive")
    private Double salary;

    @PastOrPresent(message = "CreatedAt cannot be in the future")
    private LocalDateTime createdAt;

    @PastOrPresent(message = "UpdatedAt cannot be in the future")
    private LocalDateTime updatedAt;


}


