package com.spring.jwt.StudentAttendance.repository;


import com.spring.jwt.entity.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Integer> {

    List<StudentAttendance> findByDateAndStudentClassAndTeacherId(LocalDate date, String studentClass, Integer teacherId);

    List<StudentAttendance> findByStudentClass(String studentClass);

    List<StudentAttendance> findByTeacherId(Integer teacherId);

    List<StudentAttendance> findBySub(String sub);

    List<StudentAttendance> findByDate(LocalDate date);

    List<StudentAttendance> findByUserId(Integer userId);

    List<StudentAttendance> findByUserIdAndDateBetween(Integer userId, LocalDate startDate, LocalDate endDate);

    // In StudentAttendanceRepository
    Optional<StudentAttendance> findByStudentClassAndTeacherIdAndSubAndDate(
            String studentClass, Integer teacherId, String sub, LocalDate date);

    Optional<StudentAttendance> findByDateAndSubAndUserIdAndTeacherId(
            LocalDate date, String sub, Long userId, Integer teacherId
    );
    List<StudentAttendance> findByDateAndSubAndTeacherIdAndStudentClass(
            LocalDate date, String sub, Integer teacherId, String studentClass
    );

    @Query("SELECT s.sub AS subject, " +
            "SUM(CASE WHEN s.attendance = true THEN 1 ELSE 0 END) AS presentCount, " +
            "SUM(CASE WHEN s.attendance = false THEN 1 ELSE 0 END) AS absentCount, " +
            "COUNT(s) AS totalCount " +
            "FROM StudentAttendance s " +
            "WHERE s.userId = :userId " +
            "GROUP BY s.sub")
    List<Object[]> getAttendanceSummaryByUserId(@Param("userId") Integer userId);

    StudentAttendance findFirstByUserId(Integer userId);




    @Query(value = """
    SELECT 
        CONCAT(s.name, ' ', s.last_name) AS student_name,
        s.exam AS exam,
        DATE(ed.start_time) AS start_date
    FROM student s
    LEFT JOIN exam_details ed
        ON s.student_class = ed.student_class
    WHERE s.student_class = :studentClass
      AND (:batch IS NULL OR s.batch = :batch)
    ORDER BY student_name
""", nativeQuery = true)
    List<Object[]> getStudentExamDate(
            @Param("studentClass") String studentClass,
            @Param("batch") Integer batch
    );

    @Query(value = """
                    SELECT 
                    CONCAT(s.name, ' ', s.last_name) AS student_name,
                    s.exam AS exam,
                    DATE(ed.start_time) AS start_date
                    FROM student s
                    JOIN exam_details ed
                    ON s.student_class = ed.student_class
            """, nativeQuery = true)
    List<Object[]> getAllStudentExamDate();


    // ===============================================================

    @Query(value = """
                    SELECT 
                    CONCAT(s.name, ' ', s.last_name) AS student_name,
                        s.student_class AS class,
                        s.exam AS exam,
                        u.mobile_number AS student_mobile,
                        ROUND(AVG(sa.attendance) * 100, 0) AS average_present_percentage
                    FROM student_attendance sa
                    JOIN student s ON sa.user_id = s.user_id
                    JOIN users u ON s.user_id = u.user_id
                    JOIN user_role ur ON u.user_id = ur.user_id
                    JOIN roles r ON ur.role_id = r.role_id
                    WHERE r.role_name = 'STUDENT'
                    GROUP BY 
                        s.student_id,
                        s.student_class,
                        u.mobile_number
                    ORDER BY student_name;
            """, nativeQuery = true)
    List<Object[]> getAllStudentAttendanceSummary();

    @Query(value = """
    SELECT 
        CONCAT(s.name, ' ', s.last_name) AS student_name,
        s.student_class,
        s.exam,
        u.mobile_number,
        ROUND(
            AVG(CASE WHEN sa.attendance = TRUE THEN 1 ELSE 0 END) * 100, 0
        ) AS average_present_percentage
    FROM student s
    JOIN users u ON s.user_id = u.user_id
    JOIN user_role ur ON u.user_id = ur.user_id
    JOIN roles r ON ur.role_id = r.role_id
    LEFT JOIN student_attendance sa 
        ON sa.user_id = s.user_id
    WHERE r.role_name = 'STUDENT'
      AND s.student_class = :studentClass
      AND (:batch IS NULL OR s.batch = :batch)
    GROUP BY
        s.user_id,
        s.student_class,
        s.exam,
        u.mobile_number,
        s.name,
        s.last_name
    ORDER BY student_name
""", nativeQuery = true)
    List<Object[]> getStudentAttendanceSummary(
            @Param("studentClass") String studentClass,
            @Param("batch") Integer batch
    );

    // -------------------------------------------------------------------------------------


    // Class Dropdown
    @Query(value = "SELECT DISTINCT student_class FROM student ORDER BY student_class",
            nativeQuery = true)
    List<String> getAllClasses();

    // 🔹 Student Count by Class
    @Query(value = """
    SELECT COUNT(*)
    FROM student
    WHERE student_class = :studentClass
      AND (:batch IS NULL OR batch = :batch)
""", nativeQuery = true)
    Long getStudentCountByClass(
            @Param("studentClass") String studentClass,
            @Param("batch") Integer batch
    );

    // Batch Dropdown
    @Query(value = """
        SELECT DISTINCT batch
        FROM student
        WHERE student_class = :studentClass
        ORDER BY batch
    """, nativeQuery = true)
    List<Integer> getBatchesByClass(@Param("studentClass") String studentClass);



    // -------------------------------------------------------------------------------------


    // Student Class dropdown
    @Query(value = """
        SELECT DISTINCT student_class
        FROM student
        ORDER BY student_class
    """, nativeQuery = true)
    List<String> getStudentClasses();

    // Student Name dropdown by class
    @Query(value = """
        SELECT user_id,
               CONCAT(name,' ',last_name) AS studentName
        FROM student
        WHERE student_class = :studentClass
        ORDER BY studentName
    """, nativeQuery = true)
    List<Object[]> getStudentsByClass(@Param("studentClass") String studentClass);

    // Batch dropdown by student
    @Query(value = """
        SELECT DISTINCT batch
        FROM student
        WHERE user_id = :userId
         ORDER BY batch
    """, nativeQuery = true)
    List<String> getBatchesByStudent(@Param("userId") Long userId);

    //--------------------------------------------

    // 🔹 Student Detail
    @Query(value = """
        SELECT
        
            CONCAT(s.name,' ',s.last_name) AS studentName,
            s.student_class,
            s.exam,
            s.user_id,
            ed.subject,
            er.score,
            er.total_marks,
            er.total_questions,
            er.correct_answers,
            er.incorrect_answers,
            er.unanswered_questions
        FROM student s
        JOIN exam_results er ON s.user_id = er.user_id
        JOIN exam_details ed ON er.paper_id = ed.paper_id
        WHERE s.user_id = :userId
          AND (:batch IS NULL OR s.batch = :batch)
    """, nativeQuery = true)
    List<Object[]> getStudentExamResultRaw(
            @Param("userId") Long userId,
            @Param("batch") String batch
    );

    // 🔹 Filtered List
    @Query(value = """
        SELECT
            s.user_id,
            CONCAT(s.name,' ',s.last_name),
            DATE(er.result_processed_time),
            s.exam,
            CONCAT(er.score,'/',er.total_marks)
        FROM student s
        JOIN exam_results er ON s.user_id = er.user_id
        WHERE s.student_class = :studentClass
          AND (:batch IS NULL OR s.batch = :batch)
        ORDER BY s.name
    """, nativeQuery = true)
    List<Object[]> getStudentResults(
            @Param("studentClass") String studentClass,
            @Param("batch") Integer batch
    );

    // 🔹 All Students
    @Query(value = """
        SELECT
            s.user_id,
            CONCAT(s.name,' ',s.last_name),
            DATE(er.result_processed_time),
            s.exam,
            CONCAT(er.score,'/',er.total_marks)
        FROM student s
        JOIN exam_results er ON s.user_id = er.user_id
        ORDER BY s.name
    """, nativeQuery = true)
    List<Object[]> getAllStudentResults();

}