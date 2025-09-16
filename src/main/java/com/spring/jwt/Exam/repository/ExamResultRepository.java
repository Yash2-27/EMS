package com.spring.jwt.Exam.repository;

import com.spring.jwt.Exam.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing exam results
 */
@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Integer> {
    
    /**
     * Find all results for a specific user
     * @param userId The user ID
     * @return List of exam results
     */
    List<ExamResult> findByUser_Id(Long userId);
    
    /**
     * Find all results for a specific paper
     * @param paperId The paper ID
     * @return List of exam results
     */
    List<ExamResult> findByPaper_PaperId(Integer paperId);
    
    /**
     * Find all results for a specific student class
     * @param studentClass The student class
     * @return List of exam results
     */
    List<ExamResult> findByStudentClass(String studentClass);
    
    /**
     * Find results by original session ID
     * @param sessionId The original exam session ID
     * @return The exam result
     */
    ExamResult findByOriginalSessionId(Integer sessionId);

    /**
     * Formula for Monthly Percentage
     * (SUM(score ) / SUM(total_marks )) * 100
     * @param userId
     * @return Monthly Percentage
     */
    @Query(value = """
    SELECT 
        DATE_FORMAT(e.exam_end_time, '%Y-%m') AS month,
        ROUND(
            CASE
                WHEN SUM(e.total_marks) = 0 THEN 0
                ELSE (SUM(e.score) / SUM(e.total_marks)) * 100
            END, 2
        ) AS percentage
    FROM exam_results e
    WHERE e.user_id = :userId
    GROUP BY DATE_FORMAT(e.exam_end_time, '%Y-%m')
    ORDER BY month
    """, nativeQuery = true)
    List<MonthlyPercentageProjection> findMonthlyPercentageByUser(@Param("userId") Long userId);
}

