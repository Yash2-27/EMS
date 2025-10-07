package com.spring.jwt.Exam.repository;

import com.spring.jwt.Exam.Dto.SubjectScoreReportDto;
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
     * query  for get Monthly score of student
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


    /**
     * query for get Monthly Class Average
     *
      * @param studentClass
     * @return
     */
    @Query(value = """
    SELECT 
        DATE_FORMAT(e.exam_end_time, '%Y-%m') AS month,
        ROUND(
            CASE
                WHEN SUM(e.total_marks) = 0 THEN 0
                ELSE (SUM(e.score) / SUM(e.total_marks)) * 100
            END, 2
        ) AS averagePercentage
    FROM exam_results e
    WHERE e.student_class = :studentClass
    GROUP BY DATE_FORMAT(e.exam_end_time, '%Y-%m')
    ORDER BY month
    """, nativeQuery = true)
    List<ClassMonthlyAverageProjection> findMonthlyAverageByClass(@Param("studentClass") String studentClass);

    /** query for get monthly score by subjectwise
     *
     * @param studentId
     * @param month
     * @param year
     * @return
     */
        @Query("""
        SELECT new com.spring.jwt.Exam.Dto.SubjectScoreReportDto(
            pp.subject,
            ROUND(
                CASE WHEN SUM(er.totalMarks) = 0 THEN 0
                     ELSE (SUM(er.score) / SUM(er.totalMarks)) * 100
                END, 2
            )
        )
        FROM ExamResult er
        JOIN er.paper p
        JOIN p.paperPattern pp
        JOIN Student s ON s.userId = er.user.id
        WHERE s.studentId = :studentId
          AND FUNCTION('MONTH', er.examEndTime) = :month
          AND FUNCTION('YEAR', er.examEndTime) = :year
        GROUP BY pp.subject
        """)
        List<SubjectScoreReportDto> getMonthlySubjectWiseScores(
                @Param("studentId") Long studentId,
                @Param("month") int month,
                @Param("year") int year);
}
