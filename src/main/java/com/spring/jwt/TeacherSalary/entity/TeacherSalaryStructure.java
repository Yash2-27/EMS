package com.spring.jwt.TeacherSalary.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "teacher_salary_structure")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSalaryStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long structureId;

    @Column(nullable = false, unique = true)
    private Integer teacherId;

    @Column (nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private Integer perDaySalary;

    @Column(nullable = false)
    private Integer annualSalary;

    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


}
