package com.spring.jwt.Exam.serviceImpl;

import com.spring.jwt.Exam.Dto.UpcomingExamDetailsDTO;
import com.spring.jwt.Exam.entity.UpcomingExams;
import com.spring.jwt.Exam.repository.UpcomingExamsRepository;
import com.spring.jwt.Exam.repository.PaperRepository;
import com.spring.jwt.Exam.service.UpcomingExamsService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.entity.PaperPattern;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.Exam.entity.Paper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UpcomingExamsServiceImpl implements UpcomingExamsService {

    @Autowired
    private UpcomingExamsRepository upcomingExamsRepository;

    @Autowired
    private PaperRepository paperRepository;

    private UpcomingExamDetailsDTO convertToDto(UpcomingExams exam) {
        return UpcomingExamDetailsDTO.builder()
                .title(exam.getTitle())
                .subject(exam.getSubject())
                .examDate(exam.getStartTime())
                .totalMarks(exam.getTotalMarks())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getAllUpcomingExams() {
        List<UpcomingExams> list =
                upcomingExamsRepository.findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime.now());

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No upcoming exams found at this time.");
        }

        List<UpcomingExamDetailsDTO> dtos = list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseDto.success("Upcoming exams fetched successfully", dtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getUpcomingExamsByStudentClass(String studentClass) {

        if (studentClass == null || studentClass.isBlank()) {
            throw new IllegalArgumentException("Student class must be provided");
        }

        if (!upcomingExamsRepository.existsByStudentClass(studentClass)) {
            throw new ResourceNotFoundException("Student class '" + studentClass + "' not found in the database.");
        }

        List<UpcomingExams> list =
                upcomingExamsRepository.findByStudentClassAndStartTimeAfterOrderByStartTimeAsc(
                        studentClass, LocalDateTime.now());

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No upcoming exams found for student class: " + studentClass);
        }

        List<UpcomingExamDetailsDTO> dtos = list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseDto.success("Upcoming exams fetched successfully", dtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getAllPreviousExams() {
        List<UpcomingExams> list =
                upcomingExamsRepository.findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime.now());

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No previous exams found.");
        }

        List<UpcomingExamDetailsDTO> dtos = list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseDto.success("Previous exams fetched successfully", dtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getPreviousExamsByStudentClass(String studentClass) {

        if (studentClass == null || studentClass.isBlank()) {
            throw new IllegalArgumentException("Student class must be provided");
        }

        if (!upcomingExamsRepository.existsByStudentClass(studentClass)) {
            throw new ResourceNotFoundException("Student class '" + studentClass + "' not found in the database.");
        }

        List<UpcomingExams> list =
                upcomingExamsRepository.findByStudentClassAndStartTimeBeforeOrderByStartTimeDesc(
                        studentClass, LocalDateTime.now());

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No previous exams found for class: " + studentClass);
        }

        List<UpcomingExamDetailsDTO> dtos = list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseDto.success("Previous exams fetched successfully", dtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<UpcomingExamDetailsDTO> getUpcomingExamDetailsById(Integer paperId) {
        Optional<UpcomingExams> exam = upcomingExamsRepository.findByPaper_PaperId(paperId);

        if (exam.isEmpty()) {
            throw new ResourceNotFoundException("Upcoming exam with paper ID " + paperId + " not found.");
        }

        return ResponseDto.success("Upcoming exam fetched successfully", convertToDto(exam.get()));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<UpcomingExamDetailsDTO> getUpcomingExamDetailsByUpcomingExamId(Integer upcomingExamId) {
        Optional<UpcomingExams> exam = upcomingExamsRepository.findById(upcomingExamId);

        if (exam.isEmpty()) {
            throw new ResourceNotFoundException("Upcoming exam with ID " + upcomingExamId + " not found.");
        }

        return ResponseDto.success("Upcoming exam fetched successfully", convertToDto(exam.get()));
    }

    @Override
    @Transactional
    public void createOrUpdateUpcomingExamFromPaper(Integer paperId) {

        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new ResourceNotFoundException("Paper not found with ID: " + paperId));

        PaperPattern pattern = paper.getPaperPattern();
        if (pattern == null) {
            throw new ResourceNotFoundException("PaperPattern not found for Paper ID: " + paperId);
        }

        Integer totalMarks = pattern.getMarks();
        if (totalMarks == null) {
            throw new IllegalArgumentException("Total marks missing for PaperPattern ID: " + pattern.getPaperPatternId());
        }

        String subject = pattern.getSubject();
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject missing for PaperPattern ID: " + pattern.getPaperPatternId());
        }

        Optional<UpcomingExams> existing = upcomingExamsRepository.findByPaper_PaperId(paperId);

        UpcomingExams exam = existing.orElse(new UpcomingExams());
        exam.setPaper(paper);
        exam.setTitle(paper.getTitle());
        exam.setStartTime(paper.getStartTime());
        exam.setStudentClass(paper.getStudentClass());
        exam.setTotalMarks(totalMarks);
        exam.setSubject(subject);

        upcomingExamsRepository.save(exam);
    }
}
