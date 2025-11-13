package com.spring.jwt.TeacherSalary.entity;

import jakarta.persistence.*;
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


    private Integer teacherId;


    private String month;


    private Integer year;


    private Double perDaySalary;


    private Double salary;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


}


