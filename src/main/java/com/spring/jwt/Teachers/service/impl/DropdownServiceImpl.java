package com.spring.jwt.Teachers.service.impl;

import com.spring.jwt.Teachers.exception.DropdownResourceNotFoundException;
import com.spring.jwt.Teachers.service.DropdownService;
import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
 import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DropdownServiceImpl implements DropdownService {

    private static final Logger logger = LoggerFactory.getLogger(DropdownServiceImpl.class);
    private final TeacherRepository teacherRepository;

    @Override
    public List<String> getAllClasses() {
        return Optional.ofNullable(teacherRepository.findAllClass())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException("No classes found in database."));
    }

    @Override
    public List<Object> getTeachersByClasses(String studentClass) {
        return Optional.ofNullable(teacherRepository.findTeachersByClasses(studentClass))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No teachers found for class: " + studentClass));
    }

    @Override
    public List<String> getSubjects(String studentClass, Integer teacherId) {
        return Optional.ofNullable(teacherRepository.findTeachersBySubject(studentClass, teacherId))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No subjects found for teacher ID: " + teacherId));
    }


    /**
     @Override
     public List<String> getTitles(String studentClass, Integer teacherId, String subject) {
     try {
     List<String> titles = teacherRepository.findSubjectByTitles(studentClass, teacherId, subject);
     if (titles.isEmpty()) {
     throw new DropdownResourceNotFoundException("No titles found for subject: " + subject);
     }
     return titles;
     } catch (Exception e) {
     logger.error("Error fetching titles: {}", e.getMessage());
     throw new RuntimeException("Unable to fetch titles. Please try again later.");
     }
     }
     **/


    @Override
    public List<TeacherQuestionFlatDto> getQuestionPaper(String studentClass, Integer teacherId, String subject) {
        return Optional.ofNullable(teacherRepository.findByQuestionPaper(studentClass, teacherId, subject))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No question paper found for subject: " + subject));
    }

}
