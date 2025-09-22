package com.spring.jwt.Classes;
import com.spring.jwt.entity.Classes;
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

    @Override
    public ClassesDto createClass(ClassesDto classesDto) {
        if (classesDto == null) {
            throw new IllegalArgumentException("Class data cannot be null");
        }
        Classes entity = mapper.toEntity(classesDto);
        Classes savedClass = classesRepository.save(entity);
        ClassesDto dto = mapper.toDto(savedClass);
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
        // Step 1: Check if studentClass is missing or empty
        if (studentClass == null || studentClass.trim().isEmpty()) {
            // Throw custom exception with proper message
            throw new ClassesNotFoundException("Student class not provided");
        }
        // Step 2: Get today's date
        LocalDate today = LocalDate.now();

        // Step 3: Fetch classes from repository and map to DTO
        List<ClassesDto> classes = classesRepository.findTodaysClasses(studentClass.trim(), today)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        // Step 4: If no classes found for today, throw exception
        if (classes.isEmpty()) {
            throw new ClassesNotFoundException("No classes found for today");
        }
        // Step 5: Return list of DTOs
        return classes;
    }

    @Override
    public List<ClassesDto> getUpcomingClasses(String studentClass) {
        if (studentClass == null || studentClass.trim().isEmpty()) {
            throw new ClassesNotFoundException("Student class not provided");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now(); // current date and time
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<ClassesDto> classes = classesRepository.findUpcomingClasses(studentClass.trim(), today)
                .stream()
                .map(mapper::toDto)
                .filter(c -> {
                    if (c.getDate() == null) return false;

                    // Parse class time
                    LocalTime classTime;
                    try {
                        classTime = (c.getTime() != null && !c.getTime().trim().isEmpty())
                                ? LocalTime.parse(c.getTime().trim(), timeFormatter)
                                : LocalTime.MIN; // default to start of day if time missing
                    } catch (Exception e) {
                        classTime = LocalTime.MIN;
                    }

                    // Combine date + time
                    LocalDateTime classDateTime = LocalDateTime.of(c.getDate(), classTime);

                    // Include only if classDateTime >= now
                    return !classDateTime.isBefore(now);
                })
                // Sort by date and time
                .sorted(Comparator.comparing((ClassesDto c) -> {
                    LocalTime classTime;
                    try {
                        classTime = (c.getTime() != null && !c.getTime().trim().isEmpty())
                                ? LocalTime.parse(c.getTime().trim(), timeFormatter)
                                : LocalTime.MAX;
                    } catch (Exception e) {
                        classTime = LocalTime.MAX;
                    }
                    return LocalDateTime.of(c.getDate(), classTime);
                }))
                .collect(Collectors.toList());

        if (classes.isEmpty()) {
            throw new ClassesNotFoundException("No upcoming classes found");
        }

        return classes;
    }

}