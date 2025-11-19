package com.spring.jwt.repository;

import com.spring.jwt.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByUserId(Integer userId);

    List<Student> findByStudentClass(String studentClass);

    boolean existsById(Integer id);
    List<Student> findByParent_ParentsId(Integer parentId);
    List<Student> findByStudentClassAndBatch(String studentClass, String batch);

    @Query("SELECT DISTINCT s.studentClass FROM StudentAttendance s")
    List<String> findDistinctStudentClasses();

    @Query("""
    SELECT
        s.studentClass,
        (COUNT(s) * 100.0 / (SELECT COUNT(s2) FROM Student s2))
    FROM Student s
    GROUP BY s.studentClass
    """)
    List<Object[]> countByClass();

    @Query("SELECT MONTH(s.date), s.studentClass, " +
            "ROUND((SUM(s.attendance) * 1.0 / COUNT(s)) * 100, 0) " +
            "FROM StudentAttendance s " +
            "GROUP BY MONTH(s.date), s.studentClass " +
            "ORDER BY MONTH(s.date)")
    List<Object[]> getMonthlyCount();

}
