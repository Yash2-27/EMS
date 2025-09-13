package com.spring.jwt.Exam.serviceImpl;

import com.spring.jwt.Exam.Dto.UpcomingExamDetailsDTO;
import com.spring.jwt.Exam.entity.Paper;
import com.spring.jwt.entity.PaperPattern;
import com.spring.jwt.Exam.entity.UpcomingExams;
import com.spring.jwt.Exam.repository.PaperRepository;
import com.spring.jwt.Exam.repository.UpcomingExamsRepository;
import com.spring.jwt.Exam.service.UpcomingExamsService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.ResourceNotFoundException;
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

    private UpcomingExamDetailsDTO convertToDto(UpcomingExams upcomingExam) {
        return UpcomingExamDetailsDTO.builder()
//                .upcomingExamId(upcomingExam.getUpcomingExamId())
//                .paperId(upcomingExam.getPaper().getPaperId())
                .title(upcomingExam.getTitle())
                .subject(upcomingExam.getSubject())
                .examDate(upcomingExam.getStartTime())
//                .studentClass(upcomingExam.getStudentClass())
                .totalMarks(upcomingExam.getTotalMarks())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getAllUpcomingExams() {
        List<UpcomingExams> upcomingExams = upcomingExamsRepository.findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime.now());

        if (upcomingExams.isEmpty()) {
            throw new ResourceNotFoundException("No upcoming exams found at this time.");
        }

        List<UpcomingExamDetailsDTO> dtos = upcomingExams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseDto.success("Upcoming exams fetched successfully", dtos);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getUpcomingExamsByStudentClass(String studentClass) {
        List<UpcomingExams> allMatchingClassExams = upcomingExamsRepository.findByStudentClassOrderByStartTimeAsc(studentClass);

        List<UpcomingExams> upcomingExams = allMatchingClassExams.stream()
                .filter(exam -> exam.getStartTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (upcomingExams.isEmpty()) {
            throw new ResourceNotFoundException("No upcoming exams found for student class: " + studentClass);
        }

        List<UpcomingExamDetailsDTO> dtos = upcomingExams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseDto.success("Upcoming exams for class " + studentClass + " fetched successfully", dtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getAllPreviousExams() {
        List<UpcomingExams> previousExams = upcomingExamsRepository.findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime.now());

        if (previousExams.isEmpty()) {
            throw new ResourceNotFoundException("No previous exams found.");
        }

        List<UpcomingExamDetailsDTO> dtos = previousExams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseDto.success("Previous exams fetched successfully", dtos);
    }



    @Override
    @Transactional(readOnly = true)
    public ResponseDto<List<UpcomingExamDetailsDTO>> getPreviousExamsByStudentClass(String studentClass) {
        List<UpcomingExams> foundPreviousExams = upcomingExamsRepository
                .findByStudentClassAndStartTimeBeforeOrderByStartTimeDesc(studentClass, LocalDateTime.now());

        if (foundPreviousExams.isEmpty()) {

            throw new ResourceNotFoundException("No previous exams found for student class: " + studentClass);
        }

        List<UpcomingExamDetailsDTO> previousExamDTOs = foundPreviousExams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());


        return ResponseDto.success("Previous exams for class " + studentClass + " fetched successfully", previousExamDTOs);
    }





    @Override
    @Transactional(readOnly = true)
    public ResponseDto<UpcomingExamDetailsDTO> getUpcomingExamDetailsById(Integer paperId) {
        Optional<UpcomingExams> upcomingExamOptional = upcomingExamsRepository.findByPaper_PaperId(paperId);
        if (upcomingExamOptional.isPresent()) {
            return ResponseDto.success("Upcoming exam fetched successfully", convertToDto(upcomingExamOptional.get()));
        } else {
            return ResponseDto.error("Not found", "Upcoming exam with paper ID " + paperId + " not found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<UpcomingExamDetailsDTO> getUpcomingExamDetailsByUpcomingExamId(Integer upcomingExamId) {
        Optional<UpcomingExams> upcomingExamOptional = upcomingExamsRepository.findById(upcomingExamId);
        if (upcomingExamOptional.isPresent()) {
            return ResponseDto.success("Upcoming exam fetched successfully", convertToDto(upcomingExamOptional.get()));
        } else {
            return ResponseDto.error("Not found", "Upcoming exam with ID " + upcomingExamId + " not found");
        }
    }


    @Override
    @Transactional
    public void createOrUpdateUpcomingExamFromPaper(Integer paperId) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new ResourceNotFoundException("Paper not found with ID: " + paperId));

        PaperPattern paperPattern = paper.getPaperPattern();
        if (paperPattern == null) {
            throw new ResourceNotFoundException("PaperPattern not found for Paper with ID: " + paperId + ". Cannot create UpcomingExam entry.");
        }



        Integer totalMarks = paperPattern.getMarks();
        String subject = paperPattern.getSubject();

        if (totalMarks == null) {
            throw new IllegalArgumentException("Total marks are not defined for PaperPattern with ID: " + paperPattern.getPaperPatternId() + ". Cannot create UpcomingExam entry.");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject is not defined for PaperPattern with ID: " + paperPattern.getPaperPatternId() + ". Cannot create UpcomingExam entry.");
        }

        Optional<UpcomingExams> existingUpcomingExam = upcomingExamsRepository.findByPaper_PaperId(paperId);

        UpcomingExams upcomingExam;
        if (existingUpcomingExam.isPresent()) {
            upcomingExam = existingUpcomingExam.get();
        } else {
            upcomingExam = new UpcomingExams();
        }

        upcomingExam.setPaper(paper);
        upcomingExam.setTitle(paper.getTitle());
        upcomingExam.setStartTime(paper.getStartTime());
        upcomingExam.setStudentClass(paper.getStudentClass());
        upcomingExam.setTotalMarks(totalMarks);
        upcomingExam.setSubject(subject);

        upcomingExamsRepository.save(upcomingExam);
    }
}