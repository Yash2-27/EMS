package com.spring.jwt.TeacherSalary.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "teacher_salary_monthly")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSalaryMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long salaryId;

    @Column(name = "teacher_id", nullable = false)
    private Integer teacherId;

    @Column(name = "teacher_Name" , nullable = false)
    private String teacherName;

    @Column(name = "month", length = 10, nullable = false)
    private String month;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @Column(name = "present_days", nullable = false)
    private Integer presentDays;

    @Column(name = "half_days", nullable = false)
    private Integer halfDays;

    @Column(name = "absent_days", nullable = false)
    private Integer absentDays;

    @Column(name = "late_days", nullable = false)
    private Integer lateDays;

    @Column(name = "calculated_salary", nullable = false)
    private Double calculatedSalary;

    @Column(name = "deduction", nullable = false)
    private Integer deduction;

    @Column(name = "final_salary", nullable = false)
    private Double finalSalary;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "per_day_salary", nullable = false)
    private Integer perDaySalary;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
