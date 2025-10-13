package com.spring.jwt.TeachersAttendance.service;

import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;

import java.util.List;

public interface TeacherSalaryService
{
    TeacherSalaryResponseDto calculateSalary(Integer teacherId, String month, Integer year);
    List<TeacherSalaryInfoDTO> getTeacherSummary();
}
