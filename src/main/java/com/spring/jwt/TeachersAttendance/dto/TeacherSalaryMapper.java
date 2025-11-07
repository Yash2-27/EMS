package com.spring.jwt.TeachersAttendance.dto;
import com.spring.jwt.TeachersAttendance.entity.TeacherSalary;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TeacherSalaryMapper {

    public static TeacherSalary toEntity(AddSalaryDTO dto) {

        TeacherSalary salary = new TeacherSalary();

        salary.setTeacherId(dto.getTeacherId());
        salary.setMonth(dto.getMonth());
        salary.setYear(dto.getYear());
        salary.setPerDaySalary(dto.getPerDaySalary());
        salary.setSalary(dto.getSalary());

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        salary.setCreatedAt(now);
        salary.setUpdatedAt(now);

        return salary;
    }
}


