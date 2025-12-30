package com.spring.jwt.Teachers.service.impl;


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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DropdownServiceImpl implements DropdownService {

    private static final Logger logger = LoggerFactory.getLogger(DropdownServiceImpl.class);
    private final TeacherRepository teacherRepository;

    @Override
    public List<String> getAllSubjects() {
        return Optional.ofNullable(teacherRepository.findAllSubjects())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException("No subjects found."));
    }

    @Override
    public List<String> getClassesBySubject(String subject) {
        return Optional.ofNullable(teacherRepository.findClassesBySubject(subject))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException("No classes found for subject: " + subject));
    }

    @Override
    public List<String> getIsLiveOptions(String subject, String studentClass) {
        List<Boolean> isLiveList = teacherRepository.findIsLiveByClassAndSubject(studentClass, subject);
        return isLiveList.stream().map(b -> b ? "Live" : "Past").collect(Collectors.toList());
    }

    @Override
    public List<TeacherQuestionFlatDto> getQuestionPaper(String subject, String studentClass, String isLiveStr) {
        Boolean isLive = "Live".equalsIgnoreCase(isLiveStr != null ? isLiveStr : "");
        return Optional.ofNullable(
                        teacherRepository.findQuestionPaperBySubjectClassIsLive(subject, studentClass, isLive))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No question paper found for subject: " + subject +
                                ", class: " + studentClass +
                                ", isLive: " + isLiveStr));
    }

    // Get ALL
    @Override
    public List<TeacherQuestionFlatDto> getAllQuestionPaper() {
        logger.info("Fetching all teacher questions");
        return teacherRepository.getAllQuestionPaper();
    }

    // Question Bank


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
    public List<TeacherQuestionFlatDto> getQuestionBank(String studentClass, Integer teacherId, String subject) {
        logger.info("Fetching question papers for class: {}, teacherId: {}, subject: {}",
                studentClass, teacherId, subject);

        List<TeacherQuestionFlatDto> papers = Optional.ofNullable(
                        teacherRepository.findByQuestionBank(studentClass, teacherId, subject))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new DropdownResourceNotFoundException(
                        "No question paper found for subject: " + subject));

        logger.debug("Question papers fetched: {}", papers);
        return papers;
    }


    @Override
    public List<TeacherQuestionFlatDto> getAllQuestionBank() {
        logger.info("Fetching ALL Question Bank data");
        return teacherRepository.getAllQuestionBank();
    }
}
