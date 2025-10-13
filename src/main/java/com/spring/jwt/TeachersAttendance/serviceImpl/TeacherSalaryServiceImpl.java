package com.spring.jwt.TeachersAttendance.serviceImpl;

import com.spring.jwt.Classes.ClassesRepository;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeachersAttendance.entity.TeacherSalary;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.repository.TeacherSalaryRepository;
import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
import com.spring.jwt.TeachersAttendance.service.TeacherSalaryService;
import com.spring.jwt.entity.Classes;
import com.spring.jwt.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherSalaryServiceImpl implements TeacherSalaryService {

    private final TeachersAttendanceRepository attendanceRepo;
    private final TeacherSalaryRepository teacherSalaryRepo;
    private final ClassesRepository classesRepo;

    @Override
    public TeacherSalaryResponseDto calculateSalary(Integer teacherId, String month, Integer year) {

        if (teacherId == null)
            throw new IllegalArgumentException("Teacher ID cannot be null");
        if (month == null || month.trim().isEmpty())
            throw new IllegalArgumentException("Month cannot be null or empty");
        if (year == null || year <= 0)
            throw new IllegalArgumentException("Year must be a valid positive integer");

        month = month.trim();

        // Fetch attendance
        List<TeachersAttendance> attendances = attendanceRepo.findByTeacherIdAndMonth(teacherId, month);
        if (attendances == null || attendances.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No attendance records found for teacherId " + teacherId + " for " + month + "/" + year
            );
        }

        // Fetch per day salary
        Double perDaySalary = teacherSalaryRepo.findPerDaySalary(teacherId, month, year);
        if (perDaySalary == null) {
            throw new ResourceNotFoundException(
                    "Salary record not found for teacherId " + teacherId + " for " + month + "/" + year
            );
        }

        double totalSalary = 0.0;
        Map<String, String> attendanceMap = new HashMap<>();

        for (TeachersAttendance att : attendances) {
            String markRaw = att.getMark() == null ? "ABSENT" : att.getMark();
            attendanceMap.put(att.getDate(), markRaw);

            String markNormalized = markRaw.replace(" ", "_").toUpperCase();
            switch (markNormalized) {
                case "PRESENT":
                case "FULL_DAY":
                    totalSalary += perDaySalary;
                    break;
                case "HALF_DAY":
                    totalSalary += perDaySalary / 2.0;
                    break;
                case "LATE":
                    totalSalary += perDaySalary * 0.75;
                    break;
                case "ABSENT":
                default:
                    totalSalary += 0;
                    break;
            }
        }

        TeacherSalaryResponseDto dto = new TeacherSalaryResponseDto();
        dto.setTeacherId(teacherId);
        dto.setTeacherName(attendances.get(0).getTeacherName());
        dto.setMonth(month);
        dto.setYear(year);
        dto.setPerDaySalary(perDaySalary);
        dto.setTotalSalary(totalSalary);
        dto.setAttendanceMap(attendanceMap);

        return dto;
    }

    @Override
    public List<TeacherSalaryInfoDTO> getTeacherSummary() {
        try {

            List<TeacherSalary> salaries = teacherSalaryRepo.findAll();
            if (salaries == null || salaries.isEmpty()) {
                throw new ResourceNotFoundException("No salary records found.");
            }


            List<Integer> teacherIdsInt;
            try {
                teacherIdsInt = salaries.stream()
                        .map(s -> {
                            if (s.getTeacherId() == null)
                                throw new IllegalStateException("Teacher ID cannot be null for salary record: " + s.getSalaryId());
                            return s.getTeacherId().intValue();
                        })
                        .collect(Collectors.toList());
            } catch (Exception e) {
                throw new RuntimeException("Error converting teacher IDs from Long to Integer.", e);
            }


            List<Classes> allClasses = classesRepo.findAllByTeacherIdIn(teacherIdsInt);
            if (allClasses == null) allClasses = Collections.emptyList();


            Map<Integer, List<Classes>> classesMap = allClasses.stream()
                    .collect(Collectors.groupingBy(Classes::getTeacherId));


            AtomicInteger counter = new AtomicInteger(1);
            List<TeacherSalaryInfoDTO> result = new ArrayList<>();

            for (TeacherSalary salary : salaries) {
                if (salary.getTeacherId() == null) continue;
                Integer salaryTeacherId = salary.getTeacherId().intValue();
                List<Classes> teacherClasses = classesMap.getOrDefault(salaryTeacherId, Collections.emptyList());

                if (teacherClasses.isEmpty()) {

                    result.add(new TeacherSalaryInfoDTO(
                            counter.getAndIncrement(),
                            "Unknown Teacher",
                            "N/A",
                            salary.getCreatedAt() != null ? salary.getCreatedAt().toLocalDate().toString() : "N/A",
                            salary.getSalary() != null ? salary.getSalary() : 0.0,
                            "N/A"
                    ));
                } else {
                    for (Classes cls : teacherClasses) {
                        result.add(new TeacherSalaryInfoDTO(
                                counter.getAndIncrement(),
                                cls.getTeacherName() != null ? cls.getTeacherName() : "Unknown Teacher",
                                cls.getStudentClass() != null ? cls.getStudentClass() : "N/A",
                                salary.getCreatedAt() != null ? salary.getCreatedAt().toLocalDate().toString() : "N/A",
                                salary.getSalary() != null ? salary.getSalary() : 0.0,
                                cls.getSub() != null ? cls.getSub() : "N/A"
                        ));
                    }
                }
            }

            if (result.isEmpty()) {
                throw new ResourceNotFoundException("No teacher summary data found.");
            }

            return result;

        } catch (ResourceNotFoundException rnfe) {
            throw rnfe; // rethrow known exception
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching teacher summary.", e);
        }
    }

}
