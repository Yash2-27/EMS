package com.spring.jwt.TeacherSalary.repository;


import com.spring.jwt.TeacherSalary.dto.SalaryStructureRequestDto;
import com.spring.jwt.TeacherSalary.dto.SalaryStructureResponseDto;
import com.spring.jwt.TeacherSalary.entity.TeacherSalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherSalaryStructureRepository
        extends JpaRepository<TeacherSalaryStructure, Long> {

    Optional<TeacherSalaryStructure> findByTeacherId(Integer teacherId);

    boolean existsByTeacherId(Integer teacherId);

    List<TeacherSalaryStructure> findAllByOrderByCreatedAtDesc();







}
