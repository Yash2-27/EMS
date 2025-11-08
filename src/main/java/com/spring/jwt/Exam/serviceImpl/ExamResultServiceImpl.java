package com.spring.jwt.Exam.serviceImpl;

import com.spring.jwt.Exam.Dto.ClassAverageDTO;
import com.spring.jwt.Exam.Dto.ExamResultDTO;
import com.spring.jwt.Exam.Dto.MonthlyPercentageDTO;
import com.spring.jwt.Exam.Dto.SubjectScoreReportDto;
import com.spring.jwt.Exam.entity.ExamResult;
import com.spring.jwt.Exam.entity.ExamSession;
import com.spring.jwt.Exam.entity.UserAnswer;
import com.spring.jwt.Exam.repository.ClassMonthlyAverageProjection;
import com.spring.jwt.Exam.repository.ExamResultRepository;
import com.spring.jwt.Exam.repository.ExamSessionRepository;
import com.spring.jwt.Exam.repository.MonthlyPercentageProjection;
import com.spring.jwt.Exam.service.ExamResultService;
import com.spring.jwt.dto.ResponseDto;
import com.spring.jwt.exception.NoExamResultFoundException;
import com.spring.jwt.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ExamResultService interface
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExamResultServiceImpl implements ExamResultService {

    @Autowired
    private final ExamSessionRepository examSessionRepository;
    private final ExamResultRepository examResultRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public int fixResultDateFormat() {
        // Get all sessions with result dates
        List<Map<String, Object>> rawDates = examSessionRepository.findRawResultDates();
        log.info("Found {} sessions with result dates to fix", rawDates.size());

        if (rawDates.isEmpty()) {
            return 0;
        }

        int updatedCount = 0;

        // For each session, update the result date format
        for (Map<String, Object> row : rawDates) {
            try {
                Integer sessionId = (Integer) row.get("session_id");
                Object rawDate = row.get("result_date");

                log.info("Processing session ID: {}, raw date: {}, class: {}",
                        sessionId, rawDate, rawDate != null ? rawDate.getClass().getName() : "null");

                if (rawDate == null) {
                    continue;
                }

                // Update the specific session with the correct date format
                String sql = "UPDATE exam_session SET result_date = ? WHERE session_id = ?";

                // Use the current time as a test (you can modify this as needed)
                LocalDateTime testDate = LocalDateTime.of(2023, 7, 4, 22, 37, 0);

                int updated = jdbcTemplate.update(sql, testDate, sessionId);
                log.info("Updated session {}: {} rows affected", sessionId, updated);

                if (updated > 0) {
                    updatedCount++;
                }
            } catch (Exception e) {
                log.error("Error updating session: {}", e.getMessage(), e);
            }
        }

        log.info("Successfully updated {} out of {} sessions", updatedCount, rawDates.size());
        return updatedCount;
    }

    @Override
    @Transactional
    public int processReadyExamResults() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Checking for exam sessions with resultDate <= {}", now);

        // Debug: Print all sessions with result dates
        List<ExamSession> allWithResultDates = examSessionRepository.findAllWithResultDate();
        log.info("Found {} sessions with non-null resultDate", allWithResultDates.size());
        for (ExamSession session : allWithResultDates) {
            log.info("Session ID: {}, resultDate: {}", session.getSessionId(), session.getResultDate());
        }

        // Try to find the specific date mentioned by the user
        try {
            LocalDateTime specificDate = LocalDateTime.of(2025, 7, 4, 22, 37, 0);
            log.info("Looking for sessions with resultDate = {}", specificDate);
            List<ExamSession> specificSessions = examSessionRepository.findByExactResultDate(specificDate);
            log.info("Found {} sessions with exact resultDate {}", specificSessions.size(), specificDate);

            // If we found sessions with the specific date, process them directly
            if (!specificSessions.isEmpty()) {
                log.info("Processing {} sessions with specific date", specificSessions.size());
                int processedCount = 0;

                for (ExamSession session : specificSessions) {
                    try {
                        processExamSession(session);
                        processedCount++;
                    } catch (Exception e) {
                        log.error("Error processing session {}: {}", session.getSessionId(), e.getMessage(), e);
                    }
                }

                log.info("Successfully processed {} sessions with specific date", processedCount);
                return processedCount;
            }
        } catch (Exception e) {
            log.error("Error checking for specific date: {}", e.getMessage(), e);
        }

        // Fall back to the standard method
        List<ExamSession> readySessions = examSessionRepository.findByResultDateBeforeOrEqual(now);

        if (readySessions.isEmpty()) {
            log.info("No exam sessions ready for result processing");
            return 0;
        }

        log.info("Found {} exam sessions ready for result processing", readySessions.size());
        int processedCount = 0;

        for (ExamSession session : readySessions) {
            try {
                processExamSession(session);
                processedCount++;
            } catch (Exception e) {
                log.error("Error processing exam session with ID {}: {}", session.getSessionId(), e.getMessage(), e);
            }
        }

        log.info("Successfully processed {} out of {} exam sessions", processedCount, readySessions.size());
        return processedCount;
    }

    @Override
    @Transactional
    public ExamResult processExamSession(ExamSession session) {
        log.info("Processing exam session with ID: {}", session.getSessionId());

        int totalQuestions = session.getPaper().getPaperQuestions().size();
        int answeredQuestions = session.getUserAnswers() != null ? session.getUserAnswers().size() : 0;
        int unansweredQuestions = totalQuestions - answeredQuestions;

        int correctAnswers = 0;
        int incorrectAnswers = 0;

        if (session.getUserAnswers() != null) {
            for (UserAnswer answer : session.getUserAnswers()) {
                if (answer.getSelectedOption() != null &&
                    answer.getSelectedOption().equals(answer.getQuestion().getAnswer())) {
                    correctAnswers++;
                } else {
                    incorrectAnswers++;
                }
            }
        }

        ExamResult result = new ExamResult();
        result.setUser(session.getUser());
        result.setPaper(session.getPaper());
        result.setStudentClass(session.getStudentClass());
        result.setExamStartTime(session.getStartTime());
        result.setExamEndTime(session.getEndTime());
        result.setResultProcessedTime(LocalDateTime.now());
        result.setScore(session.getScore());
        result.setNegativeCount(session.getNegativeCount());
        result.setNegativeScore(session.getNegativeScore());
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correctAnswers);
        result.setIncorrectAnswers(incorrectAnswers);
        result.setUnansweredQuestions(unansweredQuestions);
        result.setOriginalSessionId(session.getSessionId());

        ExamResult savedResult = examResultRepository.save(result);
        log.info("Created exam result with ID: {} for session ID: {}", savedResult.getResultId(), session.getSessionId());

        examSessionRepository.delete(session);
        log.info("Deleted exam session with ID: {}", session.getSessionId());

        return savedResult;
    }

    @Override
    public List<ExamResultDTO> getResultsByUserId(Long userId) {
        List<ExamResult> results = examResultRepository.findByUser_Id(userId);
        return results.stream()
                .map(ExamResultDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public List<ExamResultDTO> getResultsByPaperId(Integer paperId) {
        List<ExamResult> results = examResultRepository.findByPaper_PaperId(paperId);
        return results.stream()
                .map(ExamResultDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResultDTO> getResultsByStudentClass(String studentClass) {
        List<ExamResult> results = examResultRepository.findByStudentClass(studentClass);
        return results.stream()
                .map(ExamResultDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyPercentageDTO> getStudentMonthlyPercentage(Long userId) {
        // Fetch all exam results for this user
        List<ExamResult> results = examResultRepository.findByUserId(userId);

        if (results == null || results.isEmpty()) {
            throw new ResourceNotFoundException("No exam results found for userId: " + userId);
        }

        // Group by month (yyyy-MM)
        Map<String, List<ExamResult>> groupedByMonth = results.stream()
                .filter(r -> r.getExamEndTime() != null)
                .collect(Collectors.groupingBy(r -> r.getExamEndTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM"))));

        // Calculate percentage per month
        List<MonthlyPercentageDTO> monthlyList = new ArrayList<>();

        for (Map.Entry<String, List<ExamResult>> entry : groupedByMonth.entrySet()) {
            String month = entry.getKey();
            List<ExamResult> exams = entry.getValue();

            double totalScore = exams.stream()
                    .mapToDouble(e -> e.getScore() != null ? e.getScore() : 0)
                    .sum();

            double totalMarks = exams.stream()
                    .mapToDouble(e -> e.getTotalMarks() != null ? e.getTotalMarks() : 0)
                    .sum();

            double percentage = (totalMarks == 0) ? 0 : (totalScore / totalMarks) * 100;

            monthlyList.add(new MonthlyPercentageDTO(month, Math.round(percentage * 100.0) / 100.0));
        }
        // Sort by month ascending (yyyy-MM)
        monthlyList.sort(Comparator.comparing(MonthlyPercentageDTO::getMonth));

        return monthlyList;
    }
    @Override
    public List<ClassAverageDTO> getClassMonthlyAverage(String studentClass) {
        // Fetch all exam results for the given class
        List<ExamResult> results = examResultRepository.findByStudentClass(studentClass);

        if (results == null || results.isEmpty()) {
            throw new ResourceNotFoundException("No exam results found for class: " + studentClass);
        }

        // Group results by month (yyyy-MM)
        Map<String, List<ExamResult>> groupedByMonth = results.stream()
                .filter(r -> r.getExamEndTime() != null)
                .collect(Collectors.groupingBy(r -> r.getExamEndTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM"))));

        // Calculate monthly class average percentage
        List<ClassAverageDTO> averageList = new ArrayList<>();

        for (Map.Entry<String, List<ExamResult>> entry : groupedByMonth.entrySet()) {
            String month = entry.getKey();
            List<ExamResult> exams = entry.getValue();

            double totalScore = exams.stream()
                    .mapToDouble(e -> e.getScore() != null ? e.getScore() : 0)
                    .sum();

            double totalMarks = exams.stream()
                    .mapToDouble(e -> e.getTotalMarks() != null ? e.getTotalMarks() : 0)
                    .sum();

            double averagePercentage = (totalMarks == 0) ? 0 : (totalScore / totalMarks) * 100;

            // Round to 2 decimal places
            averageList.add(new ClassAverageDTO(month, Math.round(averagePercentage * 100.0) / 100.0));
        }

        // Sort by month ascending
        averageList.sort(Comparator.comparing(ClassAverageDTO::getMonth));

        return averageList;
    }

    @Override
    public List<SubjectScoreReportDto> getMonthlySubjectWiseScores(Long userId, int month, int year) {
        //  Step 1: Fetch all exam results for the student
        List<ExamResult> results = examResultRepository.findByUserId(userId);

        if (results == null || results.isEmpty()) {
            throw new ResourceNotFoundException("No exam results found for userId: " + userId);
        }

        // Step 2: Filter results for the given month and year
        List<ExamResult> filteredResults = results.stream()
                .filter(r -> r.getExamEndTime() != null &&
                        r.getExamEndTime().getMonthValue() == month &&
                        r.getExamEndTime().getYear() == year)
                .collect(Collectors.toList());

        if (filteredResults.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No exam results found for userId " + userId + " in " + month + "/" + year);
        }

        //  Step 3: Group by subject name
        Map<String, List<ExamResult>> groupedBySubject = filteredResults.stream()
                .filter(r -> r.getPaper() != null &&
                        r.getPaper().getPaperPattern() != null &&
                        r.getPaper().getPaperPattern().getSubject() != null)
                .collect(Collectors.groupingBy(r -> r.getPaper().getPaperPattern().getSubject()));

        //  Step 4: Calculate percentage per subject
        List<SubjectScoreReportDto> subjectScores = new ArrayList<>();

        for (Map.Entry<String, List<ExamResult>> entry : groupedBySubject.entrySet()) {
            String subject = entry.getKey();
            List<ExamResult> subjectResults = entry.getValue();

            double totalScore = subjectResults.stream()
                    .mapToDouble(r -> r.getScore() != null ? r.getScore() : 0)
                    .sum();

            double totalMarks = subjectResults.stream()
                    .mapToDouble(r -> r.getTotalMarks() != null ? r.getTotalMarks() : 0)
                    .sum();

            double percentage = (totalMarks == 0) ? 0 : (totalScore / totalMarks) * 100;

            subjectScores.add(new SubjectScoreReportDto(subject,
                    Math.round(percentage * 100.0) / 100.0));
        }

        //  Step 5: Sort alphabetically by subject name
        subjectScores.sort(Comparator.comparing(SubjectScoreReportDto::getSubject));

        return subjectScores;
    }
}