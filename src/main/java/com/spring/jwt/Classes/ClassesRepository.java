package com.spring.jwt.Classes;
import com.spring.jwt.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
public interface ClassesRepository extends JpaRepository<Classes, Long> {
    @Query("SELECT c FROM Classes c WHERE " +"c.studentClass = :studentClass AND " +
            "(" + "(:sub = 'maths' AND c.sub <> 'biology') OR " +
            "(:sub = 'biology' AND c.sub <> 'maths') OR " +
            "(:sub NOT IN ('maths', 'biology'))" +  ")")
    List<Classes> findClassBySubject(String sub, String studentClass);


    @Query("SELECT c FROM Classes c WHERE c.studentClass = :studentClass AND c.date = :today ORDER BY c.sub")
    List<Classes> findTodaysClasses(String studentClass, LocalDate today);

    @Query("SELECT c FROM Classes c WHERE c.studentClass = :studentClass AND c.date >= :today ORDER BY c.date ASC")
    List<Classes> findUpcomingClasses(String studentClass, LocalDate today);

}