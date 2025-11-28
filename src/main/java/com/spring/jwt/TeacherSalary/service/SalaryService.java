package com.spring.jwt.TeacherSalary.service;


import com.spring.jwt.TeacherSalary.dto.*;
import java.util.List;

public interface SalaryService {

    SalaryStructureResponseDto createStructure(SalaryStructureRequestDto req);


    SalaryResponseDto generateSalary(SalaryGenerateRequestDto req);

    SalaryResponseDto updateDeduction(Long salaryId, Integer deduction);

    SalaryResponseDto paySalary(Long salaryId);

    List<SalaryResponseDto> getSalaryHistory(Integer teacherId);

    SalaryResponseDto getSalaryByMonth(Integer teacherId, String month, Integer year);

    List<SalaryStructureResponseDto> getAllStructures();

    SalaryStructureResponseDto updateStructure(Integer teacherId, SalaryStructureUpdateRequestDto req);

    SalaryStructureResponseDto deactivateStructure(Integer teacherId);



}
