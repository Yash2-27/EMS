package com.spring.jwt.TeachersAttendance.service;

import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeachersAttendance.entity.TeacherSalary;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.repository.TeacherRepository;

import java.util.List;

public interface TeacherSalaryService
{
    TeacherSalaryResponseDto calculateSalary(Integer teacherId, String month, Integer year);
    List<TeacherSalaryInfoDTO> getTeacherSummary();
    TeacherSalary addTeacherSalary(TeacherSalary teacherSalary);

}
