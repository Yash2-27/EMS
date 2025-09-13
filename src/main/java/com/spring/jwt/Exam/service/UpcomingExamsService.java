package com.spring.jwt.Exam.service;

import com.spring.jwt.Exam.Dto.UpcomingExamDetailsDTO;
import com.spring.jwt.dto.ResponseDto;

import java.util.List;

public interface UpcomingExamsService {

    ResponseDto<UpcomingExamDetailsDTO> getUpcomingExamDetailsById(Integer paperId);

    ResponseDto<UpcomingExamDetailsDTO> getUpcomingExamDetailsByUpcomingExamId(Integer upcomingExamId);

    ResponseDto<List<UpcomingExamDetailsDTO>> getAllUpcomingExams();

    ResponseDto<List<UpcomingExamDetailsDTO>> getAllPreviousExams();

    // NEW METHOD: Fetch previous exams filtered by student class
    ResponseDto<List<UpcomingExamDetailsDTO>> getPreviousExamsByStudentClass(String studentClass);

    ResponseDto<List<UpcomingExamDetailsDTO>> getUpcomingExamsByStudentClass(String studentClass);

    void createOrUpdateUpcomingExamFromPaper(Integer paperId);
}