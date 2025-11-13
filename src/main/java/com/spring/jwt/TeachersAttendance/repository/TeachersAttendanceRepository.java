package com.spring.jwt.TeachersAttendance.repository;

import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeachersAttendanceRepository extends JpaRepository<TeachersAttendance, Integer> {

    Optional<TeachersAttendance> findByTeacherIdAndDate(Integer teacherId, String date);

    List<TeachersAttendance> findByTeacherId(Integer teacherId);

    List<TeachersAttendance> findByDate(String date);

    // Fetch all attendance records of a teacher for a specific month
    List<TeachersAttendance> findByTeacherIdAndMonth(Integer teacherId, String month);
}