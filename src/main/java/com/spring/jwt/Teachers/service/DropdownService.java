package com.spring.jwt.Teachers.service;

import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DropdownService {

    List<String> getAllClasses();

    List<Object> getTeachersByClasses(String studentClass);

    List<String> getSubjects(String studentClass, Integer teacherId);

    List<String> getTitles(String studentClass, Integer teacherId, String subject);

    List<TeacherQuestionFlatDto> getQuestionPaper(String studentClass, Integer teacherId, String subject, String title);

}
