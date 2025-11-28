package com.spring.jwt.Teachers.service.impl;

import com.spring.jwt.Teachers.dto.QuestionBankDTO;
import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.Teachers.service.DropdownService;
import com.spring.jwt.exception.DropdownResourceNotFoundException;
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
        logger.info("Fetching all student classes...");
        List<String> classes = Optional.ofNullable(teacherRepository.findAllClass())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException("No classes found in database."));
        logger.debug("Classes fetched: {}", classes);
        return classes;
    }

    @Override
    public List<Object> getTeachersByClasses(String studentClass) {
        logger.info("Fetching teachers for class: {}", studentClass);
        List<Object> teachers = Optional.ofNullable(teacherRepository.findTeachersByClasses(studentClass))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No teachers found for class: " + studentClass));
        logger.debug("Teachers fetched: {}", teachers);
        return teachers;
    }

    @Override
    public List<String> getSubjects(String studentClass, Integer teacherId) {
        logger.info("Fetching subjects for class: {} and teacherId: {}", studentClass, teacherId);
        List<String> subjects = Optional.ofNullable(teacherRepository.findTeachersBySubject(studentClass, teacherId))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No subjects found for teacher ID: " + teacherId));
        logger.debug("Subjects fetched: {}", subjects);
        return subjects;
    }

    @Override
    public List<TeacherQuestionFlatDto> getQuestionPaper(String studentClass, Integer teacherId, String subject) {
        logger.info("Fetching question papers for class: {}, teacherId: {}, subject: {}",
                studentClass, teacherId, subject);

        List<TeacherQuestionFlatDto> papers = Optional.ofNullable(
                        teacherRepository.findByQuestionPaper(studentClass, teacherId, subject))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No question paper found for subject: " + subject));

        logger.debug("Question papers fetched: {}", papers);
        return papers;
    }
//
//    @Override
//    public List<QuestionBankDTO> getQuestionsOnly(String studentClass, String subject, Integer teacherId) {
//        logger.info("Fetching questions without options for class: {}, subject: {}",
//                studentClass, subject);
//
//        List<QuestionBankDTO> questions = Optional.ofNullable(
//                        teacherRepository.findQuestionsOnly(studentClass, subject, teacherId))
//                .filter(list -> !list.isEmpty())
//                .orElseThrow(() -> new DropdownResourceNotFoundException(
//                        "No questions found for subject: " + subject));
//
//        logger.debug("Questions fetched: {}", questions);
//        return questions;
//
//
//    }}
}
