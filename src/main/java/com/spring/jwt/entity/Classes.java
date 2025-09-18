package com.spring.jwt.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Classes")
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classesId;

    @Column(name = "sub",nullable = false)
    @NotBlank
    private String sub;

    @Column(name = "date",nullable = false)
    private LocalDate date;

    @Column(name = "duration",nullable = false)
    @NotBlank
    private String duration;

    @Column(name = "student_Class", nullable = false)
    @NotBlank
    private String studentClass;

    @Column(name = "teacher_Id",nullable = false)
    private Integer teacherId;

    //Newly added columns or fields for Today’s Classes GET API.
    @Column(name = "topic",nullable = false)
    @NotBlank
    private String topic;

    @Column(name = "time",nullable = false)
    private LocalTime time;

    @Column(name = "teacherName",nullable = false)
    @NotBlank
    private String TeacherName;

}