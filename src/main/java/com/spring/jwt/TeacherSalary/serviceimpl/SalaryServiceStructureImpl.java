package com.spring.jwt.TeacherSalary.serviceimpl;

import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
import com.spring.jwt.TeacherSalary.dto.*;
import com.spring.jwt.TeacherSalary.entity.TeacherSalaryMonthly;
import com.spring.jwt.TeacherSalary.entity.TeacherSalaryStructure;
import com.spring.jwt.TeacherSalary.repository.TeacherSalaryMonthlyRepository;
import com.spring.jwt.TeacherSalary.repository.TeacherSalaryStructureRepository;
import com.spring.jwt.TeacherSalary.service.SalaryService;

import com.spring.jwt.entity.Teacher;
import com.spring.jwt.exception.TeacherNotFoundException;
import com.spring.jwt.exception.TeacherSalary.TeacherSalaryException;
import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryServiceStructureImpl implements SalaryService {

    private final TeacherSalaryStructureRepository structureRepo;
    private final TeacherSalaryMonthlyRepository monthlyRepo;
    private final TeachersAttendanceRepository attendanceRepo;
    private final TeacherRepository teacherRepo;




    private final ZoneId tz = ZoneId.of("Asia/Kolkata");

    private String toFullMonth(String monthInput) {
        try {
            Month m = Month.valueOf(monthInput.toUpperCase());
            return m.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        } catch (Exception e) {
            return monthInput;
        }
    }

    private String toShortMonth(String monthInput) {
        try {
            Month m = Month.valueOf(monthInput.toUpperCase());
            return m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        } catch (Exception e) {
            return monthInput.substring(0, 3).toUpperCase();
        }
    }

    // ----------------------------------------------------------
    // CREATE SALARY STRUCTURE
    // ----------------------------------------------------------
    @Override
    public SalaryStructureResponseDto createStructure(SalaryStructureRequestDto req) {

        try {
            if (req.getTeacherId() == null)
                throw new TeacherSalaryException("Teacher ID cannot be null");

            if (req.getPerDaySalary() == null)
                throw new TeacherSalaryException("Per Day Salary cannot be null");

            if (structureRepo.existsByTeacherId(req.getTeacherId()))
                throw new TeacherSalaryException("Salary structure already exists for this teacher");

            Teacher teacher = teacherRepo.findById(req.getTeacherId())
                    .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with ID: " + req.getTeacherId()));

            if (!"ACTIVE".equalsIgnoreCase(teacher.getStatus())) {
                throw new RuntimeException("Cannot create salary structure: Teacher is not ACTIVE");
            }

            TeacherSalaryStructure structure = TeacherSalaryStructure.builder()
                    .teacherId(req.getTeacherId())
                    .teacherName(teacher.getName())
                    .perDaySalary(req.getPerDaySalary())
                    .annualSalary(req.getAnnualSalary())
                    .status("ACTIVE")
                    .createdAt(LocalDateTime.now(tz))
                    .updatedAt(LocalDateTime.now(tz))
                    .build();

            structureRepo.save(structure);

            return SalaryMapper.toStructureResponse(structure);
        }
        catch (Exception e) {
            throw new TeacherSalaryException("Failed to create salary structure: " + e.getMessage());
        }
    }


    // ----------------------------------------------------------
    // UPDATE SALARY STRUCTURES
    // ----------------------------------------------------------
    @Override
    public SalaryStructureResponseDto updateStructure(
            Integer teacherId, SalaryStructureUpdateRequestDto req) {

        try {
            if (teacherId == null) {
                throw new TeacherSalaryException("Teacher ID cannot be null");
            }

            TeacherSalaryStructure structure = structureRepo.findByTeacherId(teacherId)
                    .orElseThrow(() -> new TeacherSalaryException(
                            "Salary structure not found for teacher ID: " + teacherId));

            if (req.getPerDaySalary() != null) {
                structure.setPerDaySalary(req.getPerDaySalary());
            }
            if (req.getAnnualSalary() != null) {
                structure.setAnnualSalary(req.getAnnualSalary());
            }


            if (req.getStatus() != null) {
                String newStatus = req.getStatus().toUpperCase();

                if (!newStatus.equals("ACTIVE") && !newStatus.equals("INACTIVE")) {
                    throw new TeacherSalaryException(
                            "Invalid status. Allowed values are ACTIVE or INACTIVE");
                }

                structure.setStatus(newStatus);
            }

            structure.setUpdatedAt(LocalDateTime.now(tz));
            structureRepo.save(structure);

            return SalaryMapper.toStructureResponse(structure, req);

        } catch (TeacherSalaryException e) {
            throw e;
        } catch (Exception e) {
            throw new TeacherSalaryException(
                    "Failed to update salary structure: " + e.getMessage());
        }
    }



    // ----------------------------------------------------------
    // FETCH ALL SALARY STRUCTURES
    // ----------------------------------------------------------
    @Override
    public List<SalaryStructureResponseDto> getAllStructures() {

        List<TeacherSalaryStructure> structures = structureRepo.findAllByOrderByCreatedAtDesc();

        if (structures == null || structures.isEmpty()) {
            throw new TeacherSalaryException("No salary structures found.");
        }

        List<SalaryStructureResponseDto> activeStructures = structures.stream()
                .filter(s -> {
                    Teacher teacher = teacherRepo.findById(s.getTeacherId()).orElse(null);
                    if (teacher != null) {
                        if ("ACTIVE".equalsIgnoreCase(teacher.getStatus())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                })
                .map(SalaryMapper::toStructureResponse)
                .toList();

        if (activeStructures.isEmpty()) {
            throw new TeacherSalaryException("No active teacher salary structures found.");
        }

        return activeStructures;
    }

    // ----------------------------------------------------------
    // Delete  SALARY STRUCTURES
    // ----------------------------------------------------------

    @Override
    public SalaryStructureResponseDto deactivateStructure(Integer teacherId) {
        TeacherSalaryStructure structure = structureRepo.findByTeacherId(teacherId)
                .orElseThrow(() -> new TeacherSalaryException("Salary structure not found for teacher ID: " + teacherId));

        structure.setStatus("INACTIVE");
        structure.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));

        structureRepo.save(structure);

        return SalaryMapper.toStructureResponse(structure);
    }




    // ----------------------------------------------------------
    // 3) UPDATE DEDUCTION
    // ----------------------------------------------------------
    @Override
    public SalaryResponseDto updateDeduction(Long salaryId, Integer deduction) {

        try {
            TeacherSalaryMonthly salary = monthlyRepo.findById(salaryId)
                    .orElseThrow(() -> new TeacherSalaryException("Salary record not found"));

            Integer d = deduction == null ? 0 : deduction;
            salary.setDeduction((int) Math.round(d));
            salary.setFinalSalary(salary.getCalculatedSalary() - d);

            monthlyRepo.save(salary);

            return SalaryMapper.toSalaryResponse(salary);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update deduction: " + e.getMessage());
        }
    }




}
