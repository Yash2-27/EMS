package com.spring.jwt.Question;

import com.spring.jwt.Exam.Dto.QuestionSummaryDTO;
import com.spring.jwt.entity.Question;
import com.spring.jwt.entity.enum01.QType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Question entity with pagination and sorting support
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>, JpaSpecificationExecutor<Question> {
    /**
     * Finds all questions by user id with pagination and sorting
     *
     * @param userId   the user ID
     * @param pageable pagination and sorting parameters
     * @return page of questions
     */
    Page<Question> findByUserId(Integer userId, Pageable pageable);

    /**
     * Finds all questions by user id without pagination
     *
     * @param userId the user ID
     * @return list of questions
     */
    List<Question> findByUserId(Integer userId);

    /**
     * Finds all questions matching the given specification with pagination
     *
     * @param spec     the specification to apply
     * @param pageable pagination and sorting parameters
     * @return page of questions
     */
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    /**
     * Finds all questions matching the given specification without pagination
     *
     * @param spec the specification to apply
     * @return list of questions
     */
    List<Question> findAll(Specification<Question> spec);

    /**
     * Finds a question by its id
     *
     * @param questionId the question ID
     * @return optional question
     */
    Optional<Question> findById(Integer questionId);

    /**
     * Finds questions by optional subject, type, level, and marks with pagination
     *
     * @param subject  the subject (optional)
     * @param type     the type (optional)
     * @param level    the level (optional)
     * @param marks    the marks (optional)
     * @param pageable pagination and sorting parameters
     * @return page of questions
     */
    @Query("SELECT q FROM Question q WHERE " +
            "(:subject IS NULL OR q.subject = :subject) AND " +
            "(:type IS NULL OR q.type = :type) AND " +
            "(:level IS NULL OR q.level = :level) AND " +
            "(:marks IS NULL OR q.marks = :marks)")
    Page<Question> findBySubjectTypeLevelMarks(
            @Param("subject") String subject,
            @Param("type") QType type,
            @Param("level") String level,
            @Param("marks") Integer marks,
            Pageable pageable
    );

    /**
     * Finds questions by optional subject, type, level, and marks without pagination
     *
     * @param subject the subject (optional)
     * @param type    the type (optional)
     * @param level   the level (optional)
     * @param marks   the marks (optional)
     * @return list of questions
     */
    @Query("SELECT q FROM Question q WHERE " +
            "(:subject IS NULL OR q.subject = :subject) AND " +
            "(:type IS NULL OR q.type = :type) AND " +
            "(:level IS NULL OR q.level = :level) AND " +
            "(:marks IS NULL OR q.marks = :marks)")
    List<Question> findBySubjectTypeLevelMarks(
            @Param("subject") String subject,
            @Param("type") QType type,
            @Param("level") String level,
            @Param("marks") Integer marks
    );

    boolean existsByQuestionText(String questionText);

    List<Question> findByPaper_PaperId(Integer paperId);

    @Query("SELECT pq.question FROM PaperQuestion pq WHERE pq.paper.paperId = :paperId")
    List<Question> findQuestionsByPaperId(@Param("paperId") Integer paperId);

    Page<Question> findByDeletedTrue(Pageable pageable);

    Page<Question> findByDeletedFalse(Pageable pageable);


    Page<Question> findByUserIdAndIsDescriptive(Integer userId, boolean isDescriptive, Pageable pageable);

    @Query("SELECT new com.spring.jwt.Exam.Dto.QuestionSummaryDTO(q.questionId, q.marks, q.isDescriptive) " +
            "FROM Question q WHERE q.questionId IN :questionIds")
    List<QuestionSummaryDTO> findQuestionSummariesByQuestionIds(@Param("questionIds") List<Integer> questionIds);


    @Query("SELECT q FROM Question q WHERE q.subject = :subject AND q.studentClass = :studentClass")
    List<Question> findBySubjectAndStudentClass(@Param("subject") String subject,
                                                @Param("studentClass") String studentClass);


//    @Query("""
//    SELECT new com.spring.jwt.Question.QuestionBankQuestionsDTO(
//        q.questionId, q.questionText, q.option1, q.option2, q.option3, q.option4, q.topic, q.level
//    )
//    FROM Question q
//    JOIN Teacher t ON q.userId = t.userId
//    WHERE (:studentClass IS NULL OR q.studentClass = :studentClass)
//      AND (:name IS NULL OR t.name = :name)
//      AND (:subject IS NULL OR q.subject = :subject)
//      AND (:topic IS NULL OR q.topic = :topic)
//    """)
//    List<QuestionBankQuestionsDTO> findFilteredQuestions(
//            @Param("studentClass") String studentClass,
//            @Param("name") String name,
//            @Param("subject") String subject,
//            @Param("topic") String topic
//    );

    @Query("""
        SELECT new com.spring.jwt.Question.QuestionBankQuestionsDTO(
            q.questionId,
            q.questionText,
            q.topic,
            q.level
        )
        FROM Question q
        JOIN Teacher t ON q.userId = t.userId
        WHERE (:studentClass IS NULL OR q.studentClass = :studentClass)
          AND (:name IS NULL OR t.name = :name)
          AND (:subject IS NULL OR q.subject = :subject)
          AND (:topic IS NULL OR q.topic = :topic)
    """)
    List<QuestionBankQuestionsDTO> findFilteredQuestions(
            @Param("studentClass") String studentClass,
            @Param("name") String name,
            @Param("subject") String subject,
            @Param("topic") String topic
    );


}