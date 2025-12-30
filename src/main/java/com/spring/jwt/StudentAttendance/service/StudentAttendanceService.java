package com.spring.jwt.StudentAttendance.service;

import com.spring.jwt.StudentAttendance.dto.*;
import com.spring.jwt.entity.StudentAttendance;

import java.time.LocalDate;
import java.util.List;

public interface StudentAttendanceService {
    StudentAttendanceDTO create(StudentAttendanceDTO dto);
    StudentAttendanceDTO getById(Integer id);
    List<StudentAttendanceDTO> getAll();
    StudentAttendanceDTO update(Integer id, StudentAttendanceDTO dto);
    void delete(Integer id);
//     void createBatchAttendance(CreateStudentAttendanceDTO batchDto);
 List<StudentAttendance> createBatchAttendance(CreateStudentAttendanceDTO batchDto);
    List<StudentAttendanceDTO> getByUserId(Integer userId);
    List<StudentAttendanceDTO> getByDate(LocalDate date);
    List<StudentAttendanceDTO> getBySub(String sub);
    List<StudentAttendanceDTO> getByTeacherId(Integer teacherId);
    List<StudentAttendanceDTO> getByStudentClass(String studentClass);
    List<StudentAttendanceDTO> getByDateAndStudentClassAndTeacherId(LocalDate date, String studentClass, Integer teacherId);

    AttendanceScoreDto getAttendanceScores(Integer userId);



    List<StudentAttendanceDTO> getByDateSubTeacherIdStudentClass(LocalDate date, String sub, Integer teacherId, String studentClass);

    StudentAttendanceSummaryResponseDto getSubjectWiseSummaryByUserId(Integer userId);

    List<StudentAttendanceSummaryDTO> getStudentAttendanceSummary(String studentClass, Integer batch);

    List<StudentExamDateDTO> getStudentExamDate(String studentClass, Integer batch);
    List<StudentExamDateDTO> getAllStudentExamDate();


   // Class dropdown
    List<String> getClasses();

    // Student count by selected class
    List<Long> getStudentCountByClass(String studentClass, Integer batch);

    // Batches dropdown by selected class
    List<Integer> getBatchesByClass(String studentClass);


    // Student Results By Id
    List<String> getStudentClasses();

    List<StudentDropdownDTO> getStudentsByClass(String studentClass);

    List<String> getBatchesByStudent(Long userId);

    List<StudentAttendanceSummaryDTO> getAllStudentAttendanceSummary();












    // -----------------------------------------

    List<StudentExamResultDTO> getStudentResultsById(Long userId, String batch);

    List<StudentResultsDTO> getStudentResults(String studentClass, Integer batch);

    List<StudentResultsDTO> getAllStudentResults();





}