package com.spring.jwt.ExamResult;

import com.spring.jwt.Exam.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeeExamResultRepository extends JpaRepository<ExamResult, Long> {

    @Query(value = """
        SELECT 
            q.subject AS subject,
            q.topic AS topic,
            DATE(er.exam_end_time) AS examDate,
            pp.no_of_question AS totalQuestions,
            er.total_marks AS totalMarks,
            (er.total_questions - er.unanswered_questions) AS answered,
            er.unanswered_questions AS notAnswered,
            er.score AS studentMarks,
            (SELECT COUNT(DISTINCT user_id) 
             FROM exam_results 
             WHERE paper_id = er.paper_id) AS totalStudents,
            ranked.rank AS overallRank
        FROM exam_results er
        JOIN users u ON u.user_id = er.user_id
        JOIN paper p ON p.paper_id = er.paper_id
        JOIN paper_pattern pp ON pp.paper_pattern_id = p.paper_pattern_id
        JOIN question q ON q.paper_id = p.paper_id
        JOIN (
            SELECT 
                user_id,
                paper_id,
                RANK() OVER(PARTITION BY paper_id ORDER BY score DESC) AS `rank`
            FROM exam_results
        ) AS ranked
        ON ranked.user_id = er.user_id AND ranked.paper_id = er.paper_id
        WHERE er.user_id = :userId
          AND q.subject = :subject
          AND q.topic = :topic
        LIMIT 1
        """, nativeQuery = true)
    List<Object[]> findStudentExamResult(
            @Param("userId") Long userId,
            @Param("subject") String subject,
            @Param("topic") String topic
    );

    @Query(value = "SELECT COUNT(*) FROM question q WHERE q.subject = :subject", nativeQuery = true)
    int countBySubject(@Param("subject") String subject);

    @Query(value = "SELECT COUNT(*) FROM question q WHERE q.topic = :topic", nativeQuery = true)
    int countByTopic(@Param("topic") String topic);
}
