package com.spring.jwt.TeacherSalary.service;


import com.spring.jwt.TeacherSalary.dto.*;
import java.util.List;

public interface SalaryService {

    SalaryStructureResponseDto createStructure(SalaryStructureRequestDto req);
    SalaryResponseDto updateDeduction(Long salaryId, Integer deduction);



    List<SalaryStructureResponseDto> getAllStructures();

    SalaryStructureResponseDto updateStructure(Integer teacherId, SalaryStructureUpdateRequestDto req);

    SalaryStructureResponseDto deactivateStructure(Integer teacherId);



}
