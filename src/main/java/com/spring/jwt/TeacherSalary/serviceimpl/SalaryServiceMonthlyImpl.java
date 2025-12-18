package com.spring.jwt.TeacherSalary.serviceimpl;

import com.spring.jwt.TeacherSalary.dto.*;
import com.spring.jwt.TeacherSalary.service.SalaryServiceMonthly;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;


import com.spring.jwt.TeacherSalary.entity.TeacherSalaryMonthly;
import com.spring.jwt.TeacherSalary.entity.TeacherSalaryStructure;

import com.spring.jwt.TeacherSalary.repository.TeacherSalaryMonthlyRepository;
import com.spring.jwt.TeacherSalary.repository.TeacherSalaryStructureRepository;



import com.spring.jwt.exception.TeacherSalary.TeacherSalaryException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryServiceMonthlyImpl implements SalaryServiceMonthly {

    private final TeacherSalaryStructureRepository structureRepo;
    private final TeacherSalaryMonthlyRepository monthlyRepo;
    private final TeachersAttendanceRepository attendanceRepo;


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
    //  GENERATE SALARY
    // ----------------------------------------------------------
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


            // Prevent duplicate salary
            monthlyRepo.findByTeacherIdAndMonthAndYear(req.getTeacherId(), fullMonth, req.getYear())
                    .ifPresent(m -> {
                        throw new TeacherSalaryException("Salary already generated for this month");
                    });

            // Get teacher salary structure
            TeacherSalaryStructure structure = structureRepo.findByTeacherId(req.getTeacherId())
                    .orElseThrow(() -> new TeacherSalaryException("Salary structure not found for this teacher"));

            if (!"ACTIVE".equalsIgnoreCase(structure.getStatus())) {
                throw new TeacherSalaryException("Salary structure is not ACTIVE. Cannot generate salary.");
            }

            // Attendance fetch – try Full month first then short
            List<TeachersAttendance> attendanceList =
                    attendanceRepo.findByTeacherIdAndMonth(req.getTeacherId(), fullMonth);

            if (attendanceList.isEmpty()) {
                attendanceList =
                        attendanceRepo.findByTeacherIdAndMonth(req.getTeacherId(), fullMonth);
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
                        presentDays++;
                        break;

                    case "HALF":
                    case "HALF DAY":
                    case "HALF_DAY":
                        halfDays++;
                        break;

                    case "LATE":
                        lateDays++;
                        break;

                    case "ABSENT":
                    default:
                        absentDays++;
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

            // -------------------------------
            // CREATE MONTHLY SALARY RECORD
            // -------------------------------
            TeacherSalaryMonthly saved = TeacherSalaryMonthly.builder()
                    .teacherId(req.getTeacherId())
                    .teacherName(structure.getTeacherName())
                    .month(fullMonth)
                    .year(req.getYear())
                    .calculatedSalary(calculatedSalary)
                    .deduction(deductionAmount)
                    .finalSalary(finalSalary)
                    .status("UNPAID")
                    .paymentDate(null)
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
    @Override
    public SalaryResponseDto updateMonthlySalary(Integer teacherId,
                                                 String month,
                                                 Integer year,
                                                 SalaryMonthlyUpdateRequestDto req) {

        try {

            if (req.getDeduction() == null) {
                throw new TeacherSalaryException("Deduction cannot be null");
            }
            if (req.getDeduction() < 0) {
                throw new TeacherSalaryException("Deduction cannot be negative");
            }


            if (req.getStatus() == null || req.getStatus().trim().isEmpty()) {
                throw new TeacherSalaryException("Status cannot be null or empty");
            }

            String statusInput = req.getStatus().trim().toUpperCase();
            if (!statusInput.equals("PAID") && !statusInput.equals("UNPAID")) {
                throw new TeacherSalaryException("Status must be PAID or UNPAID");
            }


            List<TeacherSalaryMonthly> salaryList = monthlyRepo.findByTeacherId(teacherId);
            if (salaryList.isEmpty()) {
                throw new TeacherSalaryException("Monthly salary record not found");
            }

            TeacherSalaryMonthly salary = salaryList.get(0);

            String newMonthShort = toShortMonth(month);
            String newMonthFull  = toFullMonth(month);
            Integer newYear = year;

            for (TeacherSalaryMonthly s : salaryList) {
                if (!s.getSalaryId().equals(salary.getSalaryId())
                        && s.getMonth().equalsIgnoreCase(newMonthShort)
                        && s.getYear().equals(newYear)) {
                    throw new TeacherSalaryException(
                            "Salary for " + newMonthShort + " " + newYear + " already exists. Choose another month."
                    );
                }
            }

            List<TeachersAttendance> attendanceList =
                    attendanceRepo.findByTeacherIdAndMonth(teacherId, newMonthFull);

            boolean existsAttendance = attendanceList.stream().anyMatch(a -> {
                try {
                    int attYear = Integer.parseInt(a.getDate().substring(0, 4));
                    return attYear == newYear;
                } catch (Exception e) {
                    return false;
                }
            });

            if (!existsAttendance) {
                throw new TeacherSalaryException(
                        "Attendance not found for " + newMonthShort + " " + newYear
                );
            }

            int createdYear = salary.getCreatedAt().getYear();
            if (!newYear.equals(createdYear)) {
                throw new TeacherSalaryException(
                        "Year cannot be changed. Salary was originally generated for year " + createdYear
                );
            }

            if (req.getStatus() != null) {
                String status = req.getStatus().trim().toUpperCase();
                if (!status.equals("PAID") && !status.equals("UNPAID")) {
                    throw new TeacherSalaryException("Status must be PAID or UNPAID");
                }
                salary.setStatus(status);
                salary.setPaymentDate(status.equals("PAID") ? LocalDateTime.now(tz) : null);
            }

            if (req.getDeduction() != null) {
                int newDeduction = req.getDeduction();
                if (newDeduction < 0) {
                    throw new TeacherSalaryException("Deduction cannot be negative");
                }
                if (newDeduction > salary.getCalculatedSalary()) {
                    throw new TeacherSalaryException("Deduction cannot exceed calculated salary");
                }
                salary.setDeduction(newDeduction);
                salary.setFinalSalary(salary.getCalculatedSalary() - newDeduction);
            }

            salary.setMonth(newMonthFull);
            salary.setYear(newYear);

            monthlyRepo.save(salary);
            return SalaryMapper.toSalaryResponse(salary);

        } catch (Exception e) {
            throw new TeacherSalaryException("Failed to update monthly salary: " + e.getMessage());
        }
    }



    // ----------------------------------------------------------
    // 4) PAY SALARY
    // ----------------------------------------------------------
    @Override
    public SalaryResponseDto paySalary(Integer teacherId, String month, Integer year) {

        try {


            String fullMonth = toFullMonth(month);

            List<TeacherSalaryMonthly> salaryList = monthlyRepo.findByTeacherId(teacherId);
            if (salaryList.isEmpty()) {
                throw new TeacherSalaryException("Salary record not found");
            }

            TeacherSalaryMonthly salary = salaryList.stream()
                    .filter(s -> s.getMonth().equalsIgnoreCase(fullMonth) && s.getYear().equals(year))
                    .findFirst()
                    .orElseThrow(() ->
                            new TeacherSalaryException("Salary for " + fullMonth + " " + year + " not found"));

            if ("PAID".equalsIgnoreCase(salary.getStatus())) {
                throw new TeacherSalaryException("Salary is already paid");
            }

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
                    .orElseThrow(() -> new TeacherSalaryException("Salary not found for this month"));

            return SalaryMapper.toSalaryResponse(salary);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to fetch salary for month: " + e.getMessage());
        }
    }


    @Override
    public List<SalaryResponseDto> getAllMonthlySalaries() {

        return monthlyRepo.findAll(
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
                .stream()
                .map(SalaryMapper::toSalaryResponse)
                .toList();
    }



    @Override
    public List<TeacherMonthlyDropdown> getActiveTeacherStructures() {

        List<TeacherMonthlyDropdown> response = new ArrayList<>();

        List<TeacherSalaryStructure> structures =
                structureRepo.findByStatus("ACTIVE");

        for (TeacherSalaryStructure structure : structures) {

            boolean hasAttendance = attendanceRepo.existsByTeacherId(structure.getTeacherId());

            if (hasAttendance) {

                // Fetch attendance list
                List<TeachersAttendance> attendanceList =
                        attendanceRepo.findByTeacherId(structure.getTeacherId());

                String month = null;
                Integer year = null;

                if (!attendanceList.isEmpty()) {
                    // Sort by date and pick latest attendance entry
                    TeachersAttendance latest = attendanceList.stream()
                            .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                            .findFirst()
                            .get();

                    // Extract month + year from attendance
                    month = latest.getMonth();
                    year = Integer.parseInt(latest.getDate().substring(0, 4));
                }

                response.add(new TeacherMonthlyDropdown(
                        structure.getTeacherId(),
                        structure.getTeacherName(),
                        month,
                        year
                ));
            }
        }

        return response;
    }




}

