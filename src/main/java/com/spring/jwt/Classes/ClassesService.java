package com.spring.jwt.Classes;
import java.util.List;
public interface ClassesService {
    ClassesDto createClass(ClassesDto classesDto);
    List<ClassesDto> getClassBySubject(String sub, String studentClass);
    ClassesDto updateClass(Long id, ClassesDto classesDto);
    void deleteClass(Long id);

    List<ClassesDto> getTodaysClasses(String studentClass);

    List<ClassesDto> getUpcomingClasses(String studentClass);
}