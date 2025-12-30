package com.spring.jwt.Teachers.service;

import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DropdownService {

    List<String> getAllSubjects();

    List<String> getClassesBySubject(String subject);

    List<String> getIsLiveOptions(String subject, String studentClass);

    List<TeacherQuestionFlatDto> getQuestionPaper(String subject, String studentClass, String isLive);

    List<TeacherQuestionFlatDto> getAllQuestionPaper();


    // Question Bank
    List<String> getAllClasses();

    List<Object> getTeachersByClasses(String studentClass);

    List<String> getSubjects(String studentClass, Integer teacherId);

    List<TeacherQuestionFlatDto> getQuestionBank(String studentClass, Integer teacherId, String subject);

    List<TeacherQuestionFlatDto> getAllQuestionBank();



}
