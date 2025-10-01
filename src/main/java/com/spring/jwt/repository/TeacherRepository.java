package com.spring.jwt.repository;

import com.spring.jwt.Teachers.dto.PapersAndTeacherInfoDto;
import com.spring.jwt.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findByUserId(Integer userId);

    @Query("SELECT new com.spring.jwt.Teachers.dto.PapersAndTeacherInfoDto(" +
            "t.name, q.subject, q.topic, q.studentClass) " +
            "FROM Teacher t JOIN Question q ON t.userId = q.userId " +
            "WHERE t.teacherId = :teacherId")
    List<PapersAndTeacherInfoDto> findByTeacherId(@Param("teacherId") Integer teacherId);

}