package com.spring.jwt.TeacherSalary.serviceimpl;

import com.spring.jwt.Classes.ClassesRepository;
import com.spring.jwt.TeacherSalary.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeacherSalary.entity.TeacherSalaryMonthly;
import com.spring.jwt.TeacherSalary.repository.TeacherSalaryRepository;
import com.spring.jwt.TeacherSalary.service.TeacherSalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.spring.jwt.entity.Classes;
import com.spring.jwt.exception.TeacherNotFoundException;
import com.spring.jwt.exception.TeacherSalary.TeacherSalaryException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalarySummaryImpl implements TeacherSalaryService {

    private final TeacherSalaryRepository teacherSalaryRepo;
    private final ClassesRepository classesRepo;

    @Override
    public List<TeacherSalaryInfoDTO> getTeacherSummary() {

        // Fetch all salary rows
        List<TeacherSalaryMonthly> salaryRecords = teacherSalaryRepo.findAll();
        if (salaryRecords.isEmpty()) {
            throw new TeacherSalaryException("No salary records available.");
        }

        // Get unique teacher IDs
        List<Integer> teacherIds = salaryRecords.stream()
                .map(record -> {
                    if (record.getTeacherId() == null) {
                        throw new TeacherNotFoundException(
                                "Teacher ID missing for salary ID: " + record.getSalaryId()
                        );
                    }
                    return record.getTeacherId();
                })
                .distinct()
                .collect(Collectors.toList());

        // Fetch all classes taught by these teachers
        List<Classes> classesList = classesRepo.findAllByTeacherIdIn(teacherIds);

        // Group classes by teacherId
        Map<Integer, List<Classes>> classesByTeacher =
                classesList.stream().collect(Collectors.groupingBy(Classes::getTeacherId));

        List<TeacherSalaryInfoDTO> summaryList = new ArrayList<>();

        // Build summary for each salary row
        for (TeacherSalaryMonthly salary : salaryRecords) {   // ✔ FIXED

            List<Classes> teacherClasses =
                    classesByTeacher.getOrDefault(salary.getTeacherId(), Collections.emptyList());

            String teacherName = teacherClasses.isEmpty()
                    ? "Unknown Teacher"
                    : teacherClasses.get(0).getTeacherName();

            String subjects = teacherClasses.stream()
                    .map(Classes::getSub)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.joining(", "));

            String classNames = teacherClasses.stream()
                    .map(Classes::getStudentClass)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.joining(", "));

            summaryList.add(
                    new TeacherSalaryInfoDTO(
                            teacherName,
                            classNames,
                            salary.getCreatedAt() != null
                                    ? salary.getCreatedAt().toLocalDate().toString()
                                    : "N/A",
                            Optional.ofNullable(salary.getCalculatedSalary()).orElse(0.0),
                            subjects
                    )
            );
        }

        // Merge duplicates (if teacher appears more than once)
        return summaryList.stream()
                .collect(Collectors.toMap(
                        TeacherSalaryInfoDTO::getTeacherName,
                        dto -> dto,
                        (oldDto, newDto) -> newDto
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }
}
