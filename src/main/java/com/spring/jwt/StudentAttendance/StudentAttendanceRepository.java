package com.spring.jwt.StudentAttendance;


import com.spring.jwt.entity.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
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
                        s.student_class AS class,
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
    List<Object[]> getStudentAttendanceSummary();

    @Query(value = """
                    SELECT 
                    CONCAT(s.name, ' ', s.last_name) AS student_name,
                    DATE(ed.start_time) AS start_date
                    FROM student s
                    JOIN exam_details ed
                    ON s.student_class = ed.student_class
            """, nativeQuery = true)
    List<Object[]> getStudentExamDate();

    @Query(value = """
                SELECT
                    CONCAT(s.name, ' ', s.last_name) AS student_name,
                    DATE(er.result_processed_time) AS result_date,
                    CONCAT(
                        COALESCE(er.score, 0),
                        '/',
                        COALESCE(er.total_marks, 0)
                    ) AS marks
                FROM student s
                JOIN exam_results er
                    ON s.user_id = er.user_id
                ORDER BY student_name
            """, nativeQuery = true)
    List<Object[]> getStudentResults();

}