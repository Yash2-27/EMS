package com.spring.jwt.TeachersAttendance.repository;

import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.entity.TeacherSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSalaryRepository extends JpaRepository<TeacherSalary, Integer> {

    // Fetch the per-day salary of a teacher for a specific month & year
    @Query("SELECT t.perDaySalary FROM TeacherSalary t WHERE t.teacherId = :teacherId AND t.month = :month AND t.year = :year")
    Double findPerDaySalary(
            @Param("teacherId") Integer teacherId,
            @Param("month") String month,
            @Param("year") Integer year
    );


}
