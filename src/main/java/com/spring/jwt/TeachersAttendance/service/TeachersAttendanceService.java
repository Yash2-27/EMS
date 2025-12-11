package com.spring.jwt.TeachersAttendance.service;
import com.spring.jwt.TeachersAttendance.dto.TeacherAttendanceUpdateDTO;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceResponseDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceSummaryDto;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;

import java.util.List;

public interface TeachersAttendanceService {
    TeachersAttendanceResponseDto createAttendance(TeachersAttendanceDto dto);

    List<TeachersAttendanceResponseDto> getAttendanceByTeacherId(Integer teacherId);

    List<TeachersAttendanceResponseDto> getAttendanceByTeacherIdAndMonth(Integer teacherId, String month);

    TeacherAttendanceUpdateDTO updateTeacherAttendance(Integer teachersAttendanceId, TeachersAttendance updatedAttendance);

    void deleteTeacherAttendance(Integer teachersAttendanceId);

    List<TeachersAttendanceResponseDto> getAttendanceByDate(String date);

    List<TeachersAttendanceResponseDto> getAttendanceByTeacherIdAndYear(Integer teacherId,String year);

    TeachersAttendanceSummaryDto getAttendanceSummaryByTeacherIdAndMonth(Integer teacherId, String month);

    List<TeachersAttendanceResponseDto> getAllAttendance();
}