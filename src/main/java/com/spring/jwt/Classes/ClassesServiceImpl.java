package com.spring.jwt.Classes;
import com.spring.jwt.entity.Classes;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ClassesServiceImpl implements ClassesService {
    @Autowired
    private ClassesRepository classesRepository;
    @Autowired
    private ClassMapper mapper;
    @Autowired
    private TeacherRepository teacherRepository;


    @Override
    public ClassesDto createClass(ClassesDto classesDto) {
        if (classesDto == null) {
            throw new IllegalArgumentException("Class data cannot be null");
        }


        if (classesDto.getSub() == null || classesDto.getSub().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject is required");
        }
        if (classesDto.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (classesDto.getDuration() == null || classesDto.getDuration().trim().isEmpty()) {
            throw new IllegalArgumentException("Duration is required");
        }
        if (classesDto.getStudentClass() == null || classesDto.getStudentClass().trim().isEmpty()) {
            throw new IllegalArgumentException("Student class is required");
        }
        if (classesDto.getTeacherId() == null) {
            throw new IllegalArgumentException("Teacher ID is required");
        }
        if (classesDto.getTime() == null || classesDto.getTime().trim().isEmpty()) {
            throw new IllegalArgumentException("Time is required");
        }
        if (classesDto.getTopic() == null || classesDto.getTopic().trim().isEmpty()) {
            throw new IllegalArgumentException("Topic is required");
        }


        Teacher teacher = teacherRepository.findById(classesDto.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Teacher with ID " + classesDto.getTeacherId() + " not found"));

        boolean exists = classesRepository.existsClassForTeacherAtTime(
                classesDto.getTeacherId(), classesDto.getDate(), classesDto.getTime());
        if (exists) {
            throw new IllegalStateException("Teacher already has a class scheduled at this time");
        }


        Classes entity = mapper.toEntity(classesDto);

        entity.setTeacherName(teacher.getName());

        Classes savedClass = classesRepository.save(entity);

        ClassesDto dto = mapper.toDto(savedClass);


        dto.setTeacherName(teacher.getName());

        return dto;
    }

    @Override
    public List<ClassesDto> getClassBySubject(String sub, String studentClass) {
        List<ClassesDto> classes = classesRepository.findClassBySubject(sub.toLowerCase(),
                studentClass).stream().map(mapper::toDto).collect(Collectors.toList());
        if (classes.isEmpty()) {
            throw new ClassesNotFoundException("Class not found");
        }
        return classes;
    }

    @Override
    public ClassesDto updateClass(Long id, ClassesDto classesDto) {
        Classes classes = classesRepository.findById(id)
                .orElseThrow(() -> new ClassesNotFoundException("Class with id : " + id + "not found"));
        if (classes.getSub() != null) {
            classes.setSub(classesDto.getSub());
        }
        if (classes.getDuration() != null) {
            classes.setDuration(classesDto.getDuration());
        }
        if (classes.getDate() != null) {
            classes.setDate(classesDto.getDate());
        }
        if (classes.getTeacherId() != null) {
            classes.setTeacherId(classesDto.getTeacherId());
        }
        if (classes.getStudentClass() != null) {
            classes.setStudentClass(classesDto.getStudentClass());
        }
        Classes savedClass = classesRepository.save(classes);
        return mapper.toDto(savedClass);
    }

    @Override
    public void deleteClass(Long id) {
        Classes classes = classesRepository.findById(id).
                orElseThrow(() -> new ClassesNotFoundException("Class does not exist"));
        classesRepository.delete(classes);
    }


    @Override
    public List<ClassesDto> getTodaysClasses(String studentClass) {

        if (studentClass == null || studentClass.trim().isEmpty()) {

            throw new ClassesNotFoundException("Student class not provided");
        }

        LocalDate today = LocalDate.now();


        List<ClassesDto> classes = classesRepository.findTodaysClasses(studentClass.trim(), today)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());


        if (classes.isEmpty()) {
            throw new ClassesNotFoundException("No classes found for today");
        }

        return classes;
    }

    @Override
    public List<ClassesDto> getUpcomingClasses(String studentClass) {
        if (studentClass == null || studentClass.trim().isEmpty()) {
            throw new ClassesNotFoundException("Student class is required");
        }

        LocalDate today = LocalDate.now();
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        List<ClassesDto> classes = classesRepository
                .findUpcomingClasses(studentClass.trim(), today, currentTime)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        if (classes.isEmpty()) {
            throw new ClassesNotFoundException("No upcoming classes found for " + studentClass);
        }

        return classes;
    }

}

