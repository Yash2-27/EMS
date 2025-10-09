package com.spring.jwt.TeachersAttendance.service;

import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;

public interface TeacherSalaryService
{
    TeacherSalaryResponseDto calculateSalary(Integer teacherId, String month, Integer year);
}
