package com.spring.jwt.TeachersAttendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private Integer teacherId;

    private String teacherName;

    private String month;

    private String date;

    private String inTime;

    private String outTime;

    private String mark;

    //its new commi
}