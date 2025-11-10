package com.spring.jwt.Classes;
import com.spring.jwt.entity.Classes;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
public interface ClassesRepository extends JpaRepository<Classes, Long> {
    @Query("SELECT COUNT(c) > 0 FROM Classes c " +
            "WHERE c.teacherId = :teacherId AND c.date = :date AND c.time = :time")
    boolean existsClassForTeacherAtTime(@Param("teacherId") Integer teacherId,
                                        @Param("date") LocalDate date,
                                        @Param("time") String time);


    @Query("SELECT c FROM Classes c WHERE " +"c.studentClass = :studentClass AND " +
            "(" + "(:sub = 'maths' AND c.sub <> 'biology') OR " +
            "(:sub = 'biology' AND c.sub <> 'maths') OR " +
            "(:sub NOT IN ('maths', 'biology'))" +  ")")
    List<Classes> findClassBySubject(String sub, String studentClass);


    @Query("SELECT c FROM Classes c WHERE c.studentClass = :studentClass AND c.date = :today ORDER BY c.sub")
    List<Classes> findTodaysClasses(String studentClass, LocalDate today);

    @Query("SELECT c FROM Classes c " + "WHERE c.studentClass = :studentClass " + "AND (c.date > :today OR (c.date = :today AND c.time >= :currentTime)) " + "ORDER BY c.date ASC, c.time ASC")
    List<Classes> findUpcomingClasses(
            @Param("studentClass") String studentClass,
            @Param("today") LocalDate today,
            @Param("currentTime") String currentTime);
    List<Classes> findAllByTeacherIdIn(List<Integer> teacherIds);

    List<Classes> findByTeacherIdAndDate(@NotNull(message = "Teacher ID is required") Integer teacherId, @NotNull(message = "Date is required") LocalDate date);
}
