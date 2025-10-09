package com.spring.jwt.TeachersAttendance.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "teacher_salary")
public class TeacherSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer salaryId;

    private Integer teacherId;

    private String month;
    private Integer year;

    private Double salary;

    private Double perDaySalary;

    // Optional: store created/updated timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}