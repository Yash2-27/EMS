//package com.spring.jwt.Question;
//
//import com.spring.jwt.entity.Classes;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public interface QuestionBankRepository extends JpaRepository<Classes, Long> {
//
//    @Query("SELECT DISTINCT new com.spring.jwt.Question.QuestionBankDTO(" +
//            "t.teacherId, t.userId, t.name, t.sub) " +
//            "FROM Classes c JOIN c.teacher t " +
//            "WHERE c.studentClass = :studentClass AND c.teacherId IS NOT NULL")
//    List<QuestionBankDTO> findTeachersByStudentClass(String studentClass);
//
//
//
//
//
//
//
//}
