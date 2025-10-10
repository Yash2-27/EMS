package com.spring.jwt.TeachersAttendance.serviceImpl;

import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
import com.spring.jwt.TeachersAttendance.service.TeacherSalaryService;
import com.spring.jwt.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeacherSalaryServiceImpl implements TeacherSalaryService {

    private final TeachersAttendanceRepository attendanceRepo;

    // Example per day salary, could be fetched from config or teacher table
    private static final double PER_DAY_SALARY = 1000.0;

    @Override
    public TeacherSalaryResponseDto calculateSalary(Integer teacherId, String month, Integer year) {
        // Fetch all attendances for the teacher for the month
        List<TeachersAttendance> attendances = attendanceRepo.findByTeacherIdAndMonth(teacherId, month);

        if (attendances.isEmpty()) {
            throw new ResourceNotFoundException("No attendance records found for teacherId " + teacherId + " for " + month + "/" + year);
        }

        double totalSalary = 0.0;
        Map<String, String> attendanceMap = new HashMap<>();

        for (TeachersAttendance att : attendances) {
            attendanceMap.put(att.getDate(), att.getMark());

            switch (att.getMark()) {
                case "FULL_DAY":
                    totalSalary += PER_DAY_SALARY;
                    break;
                case "HALF_DAY":
                    totalSalary += PER_DAY_SALARY / 2;
                    break;
                case "ABSENT":
                    totalSalary += 0;
                    break;
            }
        }
        TeacherSalaryResponseDto dto = new TeacherSalaryResponseDto();
        dto.setTeacherId(teacherId);
        dto.setTeacherName(attendances.get(0).getTeacherName()); // assume same name
        dto.setMonth(month);
        dto.setYear(year);
        dto.setPerDaySalary(PER_DAY_SALARY);
        dto.setTotalSalary(totalSalary);
        dto.setAttendanceMap(attendanceMap);

        return dto;
    }
}
