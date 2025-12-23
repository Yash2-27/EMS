//upcoming repo
package com.spring.jwt.Exam.repository;

import com.spring.jwt.Exam.entity.ExamDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface UpcomingExamsRepository extends JpaRepository<ExamDetails, Integer> {
    Optional<ExamDetails> findByPaper_PaperId(Integer paperId);
    List<ExamDetails> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime dateTime);
    List<ExamDetails> findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime dateTime);
    List<ExamDetails> findByStudentClassAndStartTimeAfterOrderByStartTimeAsc(String studentClass, LocalDateTime startTime);
    List<ExamDetails> findByStudentClassAndStartTimeBeforeOrderByStartTimeDesc(String studentClass, LocalDateTime dateTime);
    boolean existsByStudentClass(String studentClass);
}