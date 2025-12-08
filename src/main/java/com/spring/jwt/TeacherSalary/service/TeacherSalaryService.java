package com.spring.jwt.TeacherSalary.service;

import com.spring.jwt.TeacherSalary.dto.TeacherSalaryInfoDTO;
import java.util.List;

public interface TeacherSalaryService {
    List<TeacherSalaryInfoDTO> getTeacherSummary();
}
