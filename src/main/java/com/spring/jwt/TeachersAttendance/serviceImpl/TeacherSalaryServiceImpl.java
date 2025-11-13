package com.spring.jwt.TeachersAttendance.serviceImpl;

import com.spring.jwt.Classes.ClassesRepository;
import com.spring.jwt.TeachersAttendance.dto.AddSalaryDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryInfoDTO;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryMapper;
import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
import com.spring.jwt.TeachersAttendance.entity.TeacherSalary;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.repository.TeacherSalaryRepository;
import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
import com.spring.jwt.TeachersAttendance.service.TeacherSalaryService;
import com.spring.jwt.entity.Classes;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.exception.TeacherNotFoundException;
import com.spring.jwt.exception.TeacherSalary.TeacherSalaryException;
import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherSalaryServiceImpl implements TeacherSalaryService {

	private final TeachersAttendanceRepository attendanceRepo;
	private final TeacherSalaryRepository teacherSalaryRepo;
	private final ClassesRepository classesRepo;
	private final TeacherRepository teacherRepo;
    @Override
    public TeacherSalary addTeacherSalary(AddSalaryDTO dto) {
        try {
            if (dto == null) {
                throw new TeacherSalaryException("Teacher salary DTO cannot be null");
            }

            String perDayStr = dto.getPerDaySalary().toString();
            if (!perDayStr.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
                throw new TeacherSalaryException("Per-day salary must be a valid numeric value without exponential format (e.g., 1200 or 1200.50)");
            }

            if (dto.getTeacherId() == null) {
                throw new TeacherSalaryException("Teacher ID cannot be null");
            }

            String teacherIdStr = dto.getTeacherId().toString();
            if (!teacherIdStr.matches("^[0-9]+$")) {
                throw new TeacherSalaryException("Invalid Teacher ID. It must be a whole number without letters, decimals, or scientific notation.");
            }

            Integer teacherId = Integer.parseInt(teacherIdStr);
            TeacherSalary teacherSalary = TeacherSalaryMapper.toEntity(dto);

            Teacher teacher = teacherRepo.findById(teacherId)
                    .orElseThrow(() -> new TeacherNotFoundException("No teacher found with ID: " + teacherId));

            Optional<TeacherSalary> existingSalary =
                    teacherSalaryRepo.findByTeacherIdAndMonthAndYear(teacherId, dto.getMonth(), dto.getYear());

            if (existingSalary.isPresent()) {
                throw new TeacherSalaryException("Salary already exists for " + dto.getMonth() + " " + dto.getYear());
            }

            if (!"Active".equalsIgnoreCase(teacher.getStatus())) {
                throw new TeacherSalaryException("Cannot add salary. Teacher is not ACTIVE.");
            }

            Month monthEnum;
            try {
                monthEnum = Month.valueOf(dto.getMonth().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TeacherSalaryException("Invalid month name: " + dto.getMonth());
            }

            YearMonth yearMonth = YearMonth.of(dto.getYear(), monthEnum);
            int daysInMonth = yearMonth.lengthOfMonth();
            double totalSalary = dto.getPerDaySalary() * daysInMonth;

            teacherSalary.setSalary(totalSalary);
            String formatted = dto.getMonth().substring(0, 1).toUpperCase() + dto.getMonth().substring(1).toLowerCase();
            teacherSalary.setMonth(formatted);

            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            teacherSalary.setCreatedAt(now);
            teacherSalary.setUpdatedAt(now);

            YearMonth entered = YearMonth.of(dto.getYear(), Month.valueOf(dto.getMonth().toUpperCase()));
            YearMonth current = YearMonth.now(ZoneId.of("Asia/Kolkata"));
            if (entered.isBefore(current)) {
                throw new TeacherSalaryException("Salary cannot be added for past months or years.");
            }

            return teacherSalaryRepo.save(teacherSalary);

        } catch (TeacherNotFoundException | TeacherSalaryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TeacherSalaryException("Unexpected error while adding teacher salary: " + ex.getMessage(), ex);
        }
    }


    @Override
	public TeacherSalaryResponseDto calculateSalary(Integer teacherId, String month, Integer year) {

		if (teacherId == null)
			throw new TeacherSalaryException("Teacher ID cannot be null");
		if (month == null || month.trim().isEmpty())
			throw new TeacherSalaryException("Month cannot be null or empty");
		if (year == null || year <= 0)
			throw new TeacherSalaryException("Year must be a valid positive integer");

		month = month.trim();

		// Fetch attendance
		List<TeachersAttendance> attendances = attendanceRepo.findByTeacherIdAndMonth(teacherId, month);
		if (attendances == null || attendances.isEmpty()) {
			throw new TeacherNotFoundException(
					"No attendance records found for teacherId " + teacherId + " for " + month + "/" + year
			);
		}

		// Fetch per day salary
		Double perDaySalary = teacherSalaryRepo.findPerDaySalary(teacherId, month, year);
		if (perDaySalary == null) {
			throw new TeacherNotFoundException(
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
				throw new TeacherSalaryException("No salary records found.");
			}

			// Convert teacher IDs from salary table (Long to Integer)
			List<Integer> teacherIdsInt = salaries.stream()
					.map(s -> {
						if (s.getTeacherId() == null)
							throw new TeacherNotFoundException("Teacher ID cannot be null for salary record: " + s.getSalaryId());
						return s.getTeacherId().intValue();
					})
					.collect(Collectors.toList());

			// Fetch all classes linked to these teachers
			List<Classes> allClasses = classesRepo.findAllByTeacherIdIn(teacherIdsInt);
			if (allClasses == null) allClasses = Collections.emptyList();

			// Group classes by teacherId
			Map<Integer, List<Classes>> classesMap = allClasses.stream()
					.collect(Collectors.groupingBy(Classes::getTeacherId));

			AtomicInteger counter = new AtomicInteger(1);
			List<TeacherSalaryInfoDTO> result = new ArrayList<>();


			for (TeacherSalary salary : salaries) {
				if (salary.getTeacherId() == null) continue;
				Integer teacherId = salary.getTeacherId().intValue();

				List<Classes> teacherClasses = classesMap.getOrDefault(teacherId, Collections.emptyList());

				// Default values
				String teacherName = "Unknown Teacher";
				String subjects = "N/A";
				String studentClasses = "N/A";


				if (!teacherClasses.isEmpty()) {
					teacherName = teacherClasses.get(0).getTeacherName() != null
							? teacherClasses.get(0).getTeacherName()
							: "Unknown Teacher";


					subjects = teacherClasses.stream()
							.map(Classes::getSub)
							.filter(Objects::nonNull)
							.distinct()
							.collect(Collectors.joining(", "));

					studentClasses = teacherClasses.stream()
							.map(Classes::getStudentClass)
							.filter(Objects::nonNull)
							.distinct()
							.collect(Collectors.joining(", "));
				}

				// Add final DTO (one per teacher)
				result.add(new TeacherSalaryInfoDTO(
//                        counter.getAndIncrement(),
						teacherName,
						studentClasses,
						salary.getCreatedAt() != null ? salary.getCreatedAt().toLocalDate().toString() : "N/A",
						salary.getSalary() != null ? salary.getSalary() : 0.0,
						subjects
				));
			}

			if (result.isEmpty()) {
				throw new TeacherSalaryException("No teacher summary data found.");
			}


			Map<String, TeacherSalaryInfoDTO> uniqueMap = result.stream()
					.collect(Collectors.toMap(
							TeacherSalaryInfoDTO::getTeacherName,
							r -> r,
							(r1, r2) -> {

								r1.setDate(r2.getDate());
								r1.setTotalSalary(r2.getTotalSalary());
								return r1;
							}
					));

			return new ArrayList<>(uniqueMap.values());

		} catch (TeacherSalaryException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("An unexpected error occurred while fetching teacher summary.", e);
		}
	}

}
