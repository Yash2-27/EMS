package com.spring.jwt.Classes;
import com.spring.jwt.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ClassesRepository extends JpaRepository<Classes, Long> {
    @Query("SELECT c FROM Classes c WHERE " +"c.studentClass = :studentClass AND " +
            "(" + "(:sub = 'maths' AND c.sub <> 'biology') OR " +
            "(:sub = 'biology' AND c.sub <> 'maths') OR " +
            "(:sub NOT IN ('maths', 'biology'))" +  ")")
    List<Classes> findClassBySubject(String sub, String studentClass);


    //  Method for get Today’s Classes .
    @Query("SELECT c FROM Classes c " +
            "WHERE c.studentClass = :studentClass AND c.date = :today " +
            "ORDER BY c.time ASC")
    List<Classes> findTodaysClassesByStudentClass(@Param("studentClass") String studentClass,
                                                  @Param("today") LocalDate today);

    //  Method for get Upcomming Classes .
    @Query("SELECT c FROM Classes c " +
            "WHERE c.studentClass = :studentClass AND " +
            "      (c.date > :today OR (c.date = :today AND c.time > :now)) " +
            "ORDER BY c.date ASC, c.time ASC")
    List<Classes> findUpcomingClassesByStudentClass(@Param("studentClass") String studentClass,
                                                    @Param("today") LocalDate today,
                                                    @Param("now") LocalTime now);
    // check if teacher already has a class at same date & time
    boolean existsByTeacherIdAndDateAndTime(Integer teacherId, LocalDate date, LocalTime time);

}