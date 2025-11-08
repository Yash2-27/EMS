package com.spring.jwt.TeachersAttendance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers_attendance")
public class TeachersAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teachersAttendanceId;

    @NotNull(message = "Teacher ID cannot be null")
    @Positive(message = "Teacher ID must be a positive number")
    private Integer teacherId;

    private String teacherName;

    @NotBlank(message = "Month cannot be empty")
    @Pattern(
            regexp = "^(?i)(January|February|March|April|May|June|July|August|September|October|November|December)$",
            message = "Month must be a valid month name (e.g., January, February, etc.)")
    private String month;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "Date is required")
    private String date;

    @NotNull(message = "In Time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private String inTime;

    @NotNull(message = "Out Time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private String outTime;

    private String mark;
}