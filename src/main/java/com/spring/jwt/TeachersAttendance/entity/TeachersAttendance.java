package com.spring.jwt.TeachersAttendance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers_attendance") // table name should follow DB naming conventions
public class TeachersAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teachersAttendanceId;

    private Integer teacherId;

    private String teacherName;

    private String month;

    private String date;

    private String inTime;   // first punch IN

    private String outTime;  // last punch OUT

    private String mark;     // FULL_DAY, HALF_DAY, ABSENT
}
