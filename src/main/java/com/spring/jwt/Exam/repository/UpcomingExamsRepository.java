package com.spring.jwt.Exam.repository;

import com.spring.jwt.Exam.entity.UpcomingExams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UpcomingExamsRepository extends JpaRepository<UpcomingExams, Integer> {
    Optional<UpcomingExams> findByPaper_PaperId(Integer paperId);

    // used for upcoming exams
    List<UpcomingExams> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime dateTime);

    // fOr all previous
    List<UpcomingExams> findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime dateTime);

    //upcoming by student class
    List<UpcomingExams> findByStudentClassOrderByStartTimeAsc(String studentClass);

    //previous by class
    List<UpcomingExams> findByStudentClassAndStartTimeBeforeOrderByStartTimeDesc(String studentClass, LocalDateTime dateTime);
}