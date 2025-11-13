package com.spring.jwt.TeacherSalary.service;

import com.spring.jwt.TeacherSalary.dto.AddSalaryDTO;
import com.spring.jwt.TeacherSalary.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeacherSalary.entity.TeacherSalary;

import java.util.List;

public interface TeacherSalaryService
{
    TeacherSalaryResponseDto calculateSalary(Integer teacherId, String month, Integer year);
    List<TeacherSalaryInfoDTO> getTeacherSummary();
    TeacherSalary addTeacherSalary(AddSalaryDTO dto);

}
