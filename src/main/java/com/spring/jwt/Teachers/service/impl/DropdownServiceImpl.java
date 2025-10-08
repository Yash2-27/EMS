package com.spring.jwt.Teachers.service.impl;
import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.Teachers.exception.DropdownResourceNotFoundException;
import com.spring.jwt.Teachers.service.DropdownService;
import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DropdownServiceImpl implements DropdownService {

    private static final Logger logger = LoggerFactory.getLogger(DropdownServiceImpl.class);
    private final TeacherRepository teacherRepository;

    @Override
    public List<String> getAllClasses() {
        List<String> classes = teacherRepository.findAllClass();
        if (classes == null || classes.isEmpty()) {
            throw new DropdownResourceNotFoundException("No classes found in database.");
        }
        return classes;
    }

    @Override
    public List<Object> getTeachersByClasses(String studentClass) {
        try {
            List<Object> teachers = teacherRepository.findTeachersByClasses(studentClass);
            if (teachers.isEmpty()) {
                throw new DropdownResourceNotFoundException("No teachers found for class: " + studentClass);
            }
            return teachers;
        } catch (Exception e) {
            logger.error("Error fetching teachers for class {}: {}", studentClass, e.getMessage());
            throw new RuntimeException("Unable to fetch teachers. Please try again later.");
        }
    }

    @Override
    public List<String> getSubjects(String studentClass, Integer teacherId) {
        try {
            List<String> subjects = teacherRepository.findTeachersBySubject(studentClass, teacherId);
            if (subjects.isEmpty()) {
                throw new DropdownResourceNotFoundException("No subjects found for teacher ID: " + teacherId);
            }
            return subjects;
        } catch (Exception e) {
            logger.error("Error fetching subjects: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch subjects. Please try again later.");
        }
    }

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

    @Override
    public List<TeacherQuestionFlatDto> getQuestionPaper(String studentClass, Integer teacherId, String subject, String title) {
        try {
            List<TeacherQuestionFlatDto> questions = teacherRepository.findByQuestionPaper(studentClass, teacherId, subject, title);
            if (questions.isEmpty()) {
                throw new DropdownResourceNotFoundException("No questions found for title: " + title);
            }
            return questions;
        } catch (Exception e) {
            logger.error("Error fetching question paper: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch question paper. Please try again later.");
        }
    }

}
