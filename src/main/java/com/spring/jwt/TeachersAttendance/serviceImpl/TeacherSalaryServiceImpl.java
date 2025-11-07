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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

            if (dto.getTeacherId() == null) {
                throw new TeacherSalaryException("Teacher ID cannot be null");
            }

            if (dto.getPerDaySalary() == null) {
                throw new TeacherSalaryException("Per-day Salary cannot be null");
            }

            if (dto.getMonth() == null || dto.getMonth().trim().isEmpty()) {
                throw new TeacherSalaryException("Month cannot be null or empty");
            }

            if (dto.getYear() == null) {
                throw new TeacherSalaryException("Year cannot be null");
            }


            TeacherSalary teacherSalary = TeacherSalaryMapper.toEntity(dto);
            Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                    .orElseThrow(() ->
                            new TeacherNotFoundException("No teacher found with ID: " + dto.getTeacherId())
                    );

            if (!"Active".equalsIgnoreCase(teacher.getStatus())) {
                throw new TeacherSalaryException("Cannot add salary. Teacher is not ACTIVE.");
            }


            Optional<TeacherSalary> existingSalary =
                    teacherSalaryRepo.findByTeacherIdAndMonthAndYear(
                            dto.getTeacherId(), dto.getMonth(), dto.getYear()
                    );

            if (existingSalary.isPresent()) {
                throw new TeacherSalaryException(
                        "Salary already exists for " + dto.getMonth() + " " + dto.getYear()
                );
            }

            String formatted = dto.getMonth().substring(0, 1).toUpperCase()
                    + dto.getMonth().substring(1).toLowerCase();
            teacherSalary.setMonth(formatted);


            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            teacherSalary.setCreatedAt(now);
            teacherSalary.setUpdatedAt(now);

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

			// Iterate through salaries (1 per teacherId)
			for (TeacherSalary salary : salaries) {
				if (salary.getTeacherId() == null) continue;
				Integer teacherId = salary.getTeacherId().intValue();

				List<Classes> teacherClasses = classesMap.getOrDefault(teacherId, Collections.emptyList());

				// Default values
				String teacherName = "Unknown Teacher";
				String subjects = "N/A";
				String studentClasses = "N/A";

				// If class info exists for this teacher
				if (!teacherClasses.isEmpty()) {
					teacherName = teacherClasses.get(0).getTeacherName() != null
							? teacherClasses.get(0).getTeacherName()
							: "Unknown Teacher";

					// Merge all subjects & classes into comma-separated lists
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

			// Merge by teacherName if multiple salary records exist for same teacher (different months)
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

		} catch (ResourceNotFoundException rnfe) {
			throw rnfe;
		} catch (Exception e) {
			throw new RuntimeException("An unexpected error occurred while fetching teacher summary.", e);
		}
	}

}
