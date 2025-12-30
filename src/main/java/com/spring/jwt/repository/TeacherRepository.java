package com.spring.jwt.repository;


import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findByUserId(Integer userId);



    /**
    @Query("SELECT DISTINCT q.paper.title FROM Teacher t JOIN Question q ON t.userId = q.userId WHERE q.studentClass = :studentClass AND t.teacherId = :teacherId AND q.subject = :subject")
    List<String> findSubjectByTitles(@Param("studentClass") String studentClass,
                                     @Param("teacherId") Integer teacherId,
                                     @Param("subject") String subject
    );
     **/


    @Query("SELECT DISTINCT q.subject FROM Question q")
    List<String> findAllSubjects();


    @Query("SELECT DISTINCT q.studentClass FROM Question q WHERE q.subject = :subject")
    List<String> findClassesBySubject(@Param("subject") String subject);


    @Query("SELECT DISTINCT p.isLive FROM Paper p " +
            "JOIN Question q ON q.paper = p " +
            "WHERE q.studentClass = :studentClass AND q.subject = :subject")
    List<Boolean> findIsLiveByClassAndSubject(@Param("studentClass") String studentClass,
                                              @Param("subject") String subject);


    @Query("SELECT new com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto(" +
            "p.title, q.questionText, q.option1, q.option2, q.option3, q.option4, " +
            "p.isLive, p.startTime, p.endTime) " +
            "FROM Question q " +
            "JOIN Paper p ON q.paper = p " +
            "WHERE q.subject = :subject " +
            "AND q.studentClass = :studentClass " +
            "AND p.isLive = :isLive")
    List<TeacherQuestionFlatDto> findQuestionPaperBySubjectClassIsLive(
            @Param("subject") String subject,
            @Param("studentClass") String studentClass,
            @Param("isLive") Boolean isLive
    );


    @Query("SELECT new com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto(" +
            "p.title, q.questionText, q.option1, q.option2, q.option3, q.option4, " +
            "p.isLive, p.startTime, p.endTime) " +
            "FROM Teacher t " +
            "JOIN Question q ON t.userId = q.userId " +
            "JOIN Paper p ON q.paper = p " +
            "WHERE q.studentClass = :studentClass " +
            "AND t.teacherId = :teacherId " +
            "AND p.isLive = :isLive")
    List<TeacherQuestionFlatDto> findByQuestionPaper(
            @Param("studentClass") String studentClass,
            @Param("teacherId") Integer teacherId,
            @Param("isLive") Boolean isLive
    );

    @Query("""
            SELECT new com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto(
                p.title,
                q.questionText,
                q.option1,
                q.option2,
                q.option3,
                q.option4,
                p.isLive,
                p.startTime,
                p.endTime
            )
            FROM Teacher t
            JOIN Question q ON t.userId = q.userId
            JOIN Paper p ON q.paper = p
        """)
    List<TeacherQuestionFlatDto> getAllQuestionPaper();


    // Question Bank

    @Query("SELECT DISTINCT t.studentClass FROM Question t")
    List<String> findAllClass();

    @Query("SELECT DISTINCT t.teacherId, t.name FROM Teacher t JOIN Question q ON t.userId = q.userId WHERE q.studentClass = :studentClass")
    List<Object> findTeachersByClasses(@Param("studentClass") String studentClass);

    @Query("SELECT DISTINCT q.subject FROM Teacher t JOIN Question q ON t.userId = q.userId WHERE q.studentClass = :studentClass AND t.teacherId = :teacherId")
    List<String> findTeachersBySubject(@Param("studentClass") String studentClass,
                                       @Param("teacherId") Integer teacherId
    );

    @Query("SELECT new com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto(" +
            "p.title, q.questionText, q.option1, q.option2, q.option3, q.option4, " +
            "p.isLive, p.startTime, p.endTime) " +
            "FROM Teacher t " +
            "JOIN Question q ON t.userId = q.userId " +
            "JOIN Paper p ON q.paper = p " +
            "WHERE p.studentClass = :studentClass " +
            "AND t.teacherId = :teacherId " +
            "AND q.subject = :subject ")
    List<TeacherQuestionFlatDto> findByQuestionBank(
            @Param("studentClass") String studentClass,
            @Param("teacherId") Integer teacherId,
            @Param("subject") String subject
    );

    @Query("""
              SELECT new com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto(
                p.title,
                q.questionText,
                q.option1,
                q.option2,
                q.option3,
                q.option4,
                p.isLive,
                p.startTime,
                p.endTime
            )
            FROM Teacher t
            JOIN Question q ON t.userId = q.userId
            JOIN Paper p ON q.paper = p
        """)
    List<TeacherQuestionFlatDto> getAllQuestionBank();


}