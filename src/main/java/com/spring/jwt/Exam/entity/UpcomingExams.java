package com.spring.jwt.Exam.entity;

//upcoming entity

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ExamDetails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingExams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DetailsId") // Primary Key
    private Integer upcomingExamId;

    // OneToOne relationship to Paper
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paper_id", referencedColumnName = "paperId", nullable = false, unique = true)
    private Paper paper;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "student_class", nullable = false)
    private String studentClass;

    @Column(name = "total_marks")
    private Integer totalMarks;

    @Column(name = "subject")
    private String subject;
}