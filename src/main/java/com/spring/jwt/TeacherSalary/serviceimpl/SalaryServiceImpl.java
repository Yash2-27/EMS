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
public class SalaryServiceImpl implements SalaryService {

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

            if (!"ACTIVE".equalsIgnoreCase(structure.getStatus())) {
                throw new TeacherSalaryException(
                        "Cannot update salary structure: Salary structure is INACTIVE");
            }

            // Update only the fields that are provided
            if (req.getPerDaySalary() != null) {
                structure.setPerDaySalary(req.getPerDaySalary());
            }

            if (req.getAnnualSalary() != null) {
                structure.setAnnualSalary(req.getAnnualSalary());
            }

            structure.setUpdatedAt(LocalDateTime.now(tz));

            structureRepo.save(structure);

            // Use the new mapper method for partial update
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


    // ----------------------------------------------------------//
    // 2) GENERATE SALARY                                        //
    // ----------------------------------------------------------//
    @Override
    public SalaryResponseDto generateSalary(SalaryGenerateRequestDto req) {

        try {
            if (req.getTeacherId() == null)
                throw new TeacherSalaryException("Teacher ID cannot be null");

            if (req.getMonth() == null)
                throw new TeacherSalaryException("Month cannot be null");

            if (req.getYear() == null)
                throw new TeacherSalaryException("Year cannot be null");

            String fullMonth = toFullMonth(req.getMonth());
            String shortMonth = toShortMonth(req.getMonth());

            monthlyRepo.findByTeacherIdAndMonthAndYear(req.getTeacherId(), shortMonth, req.getYear())
                    .ifPresent(m -> {
                        throw new TeacherSalaryException("Salary already generated for this month");
                    });

            TeacherSalaryStructure structure = structureRepo.findByTeacherId(req.getTeacherId())
                    .orElseThrow(() -> new TeacherSalaryException("Salary structure not found for this teacher"));


            if (!"ACTIVE".equalsIgnoreCase(structure.getStatus())) {
                throw new TeacherSalaryException("Salary structure is not ACTIVE. Cannot generate salary.");
            }



            List<TeachersAttendance> attendanceList =
                    attendanceRepo.findByTeacherIdAndMonth(req.getTeacherId(), fullMonth);

            if (attendanceList.isEmpty()) {
                attendanceList =
                        attendanceRepo.findByTeacherIdAndMonth(req.getTeacherId(), shortMonth);
            }


            if (attendanceList.isEmpty()) {
                throw new TeacherSalaryException("No attendance found for this month");
            }

            int presentDays = 0;
            int absentDays = 0;
            int halfDays = 0;
            int lateDays = 0;

            for (TeachersAttendance a : attendanceList) {

                String mark = a.getMark().trim().toUpperCase();

                switch (mark) {
                    case "PRESENT":
                    case "FULL_DAY":
                        presentDays += 1;
                        break;

                    case "HALF":
                    case "HALF DAY":
                    case "HALF_DAY":
                        halfDays += 1;
                        break;

                    case "LATE":
                        lateDays += 1;
                        break;

                    case "ABSENT":
                    default:
                        absentDays += 1;
                }
            }

            int totalDays = presentDays + absentDays + halfDays + lateDays;

            Integer perDay = structure.getPerDaySalary();

            double calculatedSalary = perDay * totalDays;

            int deductionAmount =
                    (int) ((absentDays * perDay) +
                            (halfDays * (perDay * 0.5)) +
                            (lateDays * (perDay * 0.25)));

            double finalSalary = calculatedSalary - deductionAmount;

            TeacherSalaryMonthly saved = TeacherSalaryMonthly.builder()
                    .teacherId(req.getTeacherId())
                    .month(shortMonth)
                    .year(req.getYear())
                    .calculatedSalary(calculatedSalary)
                    .deduction(deductionAmount)
                    .finalSalary(finalSalary)
                    .status("UNPAID")
                    .createdAt(LocalDateTime.now(tz))
                    .presentDays(presentDays)
                    .absentDays(absentDays)
                    .halfDays(halfDays)
                    .lateDays(lateDays)
                    .perDaySalary(structure.getPerDaySalary())
                    .totalDays(totalDays)
                    .build();

            monthlyRepo.save(saved);

            return SalaryMapper.toSalaryResponse(saved);
        }
        catch (Exception e) {
            throw new TeacherSalaryException("Failed to generate salary: " + e.getMessage());
        }
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
            salary.setDeduction((int) Math.round(d)); // if deduction field is Integer
            salary.setFinalSalary(salary.getCalculatedSalary() - d);

            monthlyRepo.save(salary);

            return SalaryMapper.toSalaryResponse(salary);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update deduction: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------
    // 4) PAY SALARY
    // ----------------------------------------------------------
    @Override
    public SalaryResponseDto paySalary(Long salaryId) {

        try {
            TeacherSalaryMonthly salary = monthlyRepo.findById(salaryId)
                    .orElseThrow(() -> new RuntimeException("Salary record not found"));

            if ("PAID".equalsIgnoreCase(salary.getStatus()))
                throw new TeacherSalaryException("Salary is already paid");

            salary.setStatus("PAID");
            salary.setPaymentDate(LocalDateTime.now(tz));

            monthlyRepo.save(salary);

            return SalaryMapper.toSalaryResponse(salary);
        } catch (Exception e) {
            throw new TeacherSalaryException("Failed to update salary payment status: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------
    // SALARY HISTORY
    // ----------------------------------------------------------
    @Override
    public List<SalaryResponseDto> getSalaryHistory(Integer teacherId) {
        try {
            return monthlyRepo.findByTeacherId(teacherId)
                    .stream()
                    .map(SalaryMapper::toSalaryResponse)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new TeacherSalaryException("Failed to fetch salary history: " + e.getMessage());
        }
    }


    // ----------------------------------------------------------
    // GET SALARY BY MONTH
    // ----------------------------------------------------------
    @Override
    public SalaryResponseDto getSalaryByMonth(Integer teacherId, String month, Integer year) {

        try {
            String shortMonth = toShortMonth(month);

            TeacherSalaryMonthly salary = monthlyRepo
                    .findByTeacherIdAndMonthAndYear(teacherId, shortMonth, year)
                    .orElseThrow(() -> new RuntimeException("Salary not found for this month"));

            return SalaryMapper.toSalaryResponse(salary);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to fetch salary for month: " + e.getMessage());
        }
    }



}
