package com.spring.jwt.repository;

import com.spring.jwt.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByUserId(Integer userId);

    List<Student> findByStudentClass(String studentClass);

    boolean existsById(Integer id);
    List<Student> findByParent_ParentsId(Integer parentId);
    List<Student> findByStudentClassAndBatch(String studentClass, String batch);

    @Query(value = """
    SELECT
        student_class,
        (COUNT(*) * 100.0 / (SELECT COUNT(*) FROM student))
    FROM student
    GROUP BY student_class
""", nativeQuery = true)
    List<Object[]> countByClass();

    @Query(value = """
                    SELECT
                        MONTH(sa.date) AS month,
                        sa.student_class AS student_class,
                        ROUND((SUM(CAST(sa.attendance AS UNSIGNED)) * 100.0 / COUNT(*)), 0) AS pct
                    FROM student_attendance sa
                    JOIN student s ON sa.user_id = s.user_id
                    WHERE (:studentClass IS NULL OR :studentClass = '' OR sa.student_class = :studentClass)
                      AND (:exam IS NULL OR :exam = '' OR s.exam = :exam)
                    GROUP BY MONTH(sa.date), sa.student_class
                    ORDER BY MONTH(sa.date)
                """, nativeQuery = true)
    List<Object[]> getMonthlyCountFiltered(
            @Param("studentClass") String studentClass,
            @Param("exam") String exam
    );


    @Query(value = "SELECT DISTINCT sa.student_class FROM student_attendance sa WHERE sa.student_class IN ('11', '12')", nativeQuery = true)
    List<String> findDistinctStudentClasses();

    @Query(value = """
    SELECT DISTINCT s.exam
    FROM student s
    WHERE s.student_class IN ('11', '12')
    """, nativeQuery = true)
    List<String> findByStudentBatch();
}
