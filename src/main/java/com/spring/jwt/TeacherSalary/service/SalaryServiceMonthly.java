package com.spring.jwt.TeacherSalary.service;

import com.spring.jwt.TeacherSalary.dto.SalaryGenerateRequestDto;
import com.spring.jwt.TeacherSalary.dto.SalaryMonthlyUpdateRequestDto;
import com.spring.jwt.TeacherSalary.dto.SalaryResponseDto;
import com.spring.jwt.TeacherSalary.dto.TeacherMonthlyDropdown;

import java.util.List;

public interface SalaryServiceMonthly {

    SalaryResponseDto generateSalary(SalaryGenerateRequestDto req);

    SalaryResponseDto updateMonthlySalary(Integer teacherId, String month, Integer year, SalaryMonthlyUpdateRequestDto req);

    SalaryResponseDto paySalary(Integer teacherId, String month, Integer year);


    List<SalaryResponseDto> getSalaryHistory(Integer teacherId);

    SalaryResponseDto getSalaryByMonth(Integer teacherId, String month, Integer year);

    List<SalaryResponseDto> getAllMonthlySalaries();


    List<TeacherMonthlyDropdown> getActiveTeacherStructures();


}
