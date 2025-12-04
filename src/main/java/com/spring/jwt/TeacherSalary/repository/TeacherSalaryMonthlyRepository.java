package com.spring.jwt.TeacherSalary.repository;



import com.spring.jwt.TeacherSalary.entity.TeacherSalaryMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface TeacherSalaryMonthlyRepository
        extends JpaRepository<TeacherSalaryMonthly, Long> {

    Optional<TeacherSalaryMonthly> findByTeacherIdAndMonthAndYear(Integer teacherId, String month, Integer year);

    List<TeacherSalaryMonthly> findByTeacherId(Integer teacherId);



}
