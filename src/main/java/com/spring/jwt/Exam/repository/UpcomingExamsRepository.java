//upcoming repo
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
    List<UpcomingExams> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime dateTime);
    List<UpcomingExams> findByStartTimeBeforeOrderByStartTimeDesc(LocalDateTime dateTime);
    List<UpcomingExams> findByStudentClassAndStartTimeAfterOrderByStartTimeAsc(String studentClass, LocalDateTime startTime);
    List<UpcomingExams> findByStudentClassAndStartTimeBeforeOrderByStartTimeDesc(String studentClass, LocalDateTime dateTime);
    boolean existsByStudentClass(String studentClass);
}