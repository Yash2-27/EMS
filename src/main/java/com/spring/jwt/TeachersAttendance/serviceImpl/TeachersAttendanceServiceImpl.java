package com.spring.jwt.TeachersAttendance.serviceImpl;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceResponseDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceSummaryDto;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
import com.spring.jwt.TeachersAttendance.service.TeachersAttendanceService;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.exception.BadRequestException;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeachersAttendanceServiceImpl implements TeachersAttendanceService {

    private final TeachersAttendanceRepository attendanceRepo;
    private final TeacherRepository teacherRepo;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    @Override
    public TeachersAttendanceResponseDto createAttendance(TeachersAttendanceDto dto) {
        try {
            //  Validate input
            if (dto.getTeacherId() == null) {
                throw new BadRequestException("Teacher ID cannot be null");
            }

            // Fetch teacher info
            Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Teacher not found with ID: " + dto.getTeacherId()
                    ));

            //  Current date (Asia/Kolkata)
            LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentDate.format(dateFormatter);

            //  Current time
            LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Kolkata"));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);

            // Check if attendance exists
            TeachersAttendance attendance = attendanceRepo
                    .findByTeacherIdAndDate(dto.getTeacherId(), formattedDate)
                    .orElseGet(() -> {
                        TeachersAttendance a = new TeachersAttendance();
                        a.setTeacherId(teacher.getTeacherId());
                        a.setTeacherName(teacher.getName());
                        a.setDate(formattedDate);
                        a.setMonth(currentDate.getMonth().name());
                        return a;
                    });

            //  Set In/Out time
            if (attendance.getInTime() == null) {
                attendance.setInTime(formattedTime);
            }
            attendance.setOutTime(formattedTime);

            //  Calculate hours and mark
            double hours = calculateHours(attendance.getInTime(), attendance.getOutTime());
            attendance.setMark(getMark(hours));

            //  Save attendance
            TeachersAttendance saved = attendanceRepo.save(attendance);

            //   Prepare response
            TeachersAttendanceResponseDto response = new TeachersAttendanceResponseDto();
            response.setAttendanceId(saved.getTeachersAttendanceId());
            response.setTeacherId(saved.getTeacherId());
            response.setTeacherName(saved.getTeacherName());
            response.setDate(saved.getDate());
            response.setMonth(saved.getMonth());
            response.setInTime(saved.getInTime());
            response.setOutTime(saved.getOutTime());
            response.setMark(saved.getMark());

            return response;
        }
        //  Known exceptions (custom messages)
        catch (IllegalArgumentException e) {
            System.err.println(" Error: " + e.getMessage());
            throw e; // rethrow so controller can handle properly
        }
        catch (ResourceNotFoundException e) {
            System.err.println(" Error: " + e.getMessage());
            throw e;
        }

        catch ( BadRequestException e) {
            throw e;
        }


//        catch (Exception e) {
//            System.err.println("Unexpected error occurred while creating attendance: " + e.getMessage());
//            throw new RuntimeException("Failed to create attendance due to unexpected error.", e);
//        }

    }


    private double calculateHours(String inTime, String outTime) {
        LocalTime in = LocalTime.parse(inTime, timeFormatter);
        LocalTime out = LocalTime.parse(outTime, timeFormatter);
        Duration duration = Duration.between(in, out);
        return duration.toMinutes() / 60.0; // hours as decimal
    }

    private String getMark(double hours) {
        // 15–20 min grace (0.25–0.33 hr)
        double fullDayThreshold = 8 - 0.33;  // 7 hr 40 min = FULL_DAY
        double halfDayThreshold = 4 - 0.33;  // 3 hr 40 min = HALF_DAY

        if (hours >= fullDayThreshold) return "FULL_DAY";
        if (hours >= halfDayThreshold) return "HALF_DAY";
        return "ABSENT";
    }

    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByTeacherId(Integer teacherId) {
        List<TeachersAttendanceResponseDto> responseList = new ArrayList<>();
        try {
            if (teacherId == null) {
                throw new IllegalArgumentException("Teacher ID cannot be null");
            }

            List<TeachersAttendance> attendances = attendanceRepo.findByTeacherId(teacherId);
            if (attendances == null || attendances.isEmpty()) {
                throw new ResourceNotFoundException("No attendance found for Teacher ID: " + teacherId);
            }

            responseList = attendances.stream().map(att -> {
                TeachersAttendanceResponseDto dto = new TeachersAttendanceResponseDto();
                dto.setAttendanceId(att.getTeachersAttendanceId());
                dto.setTeacherId(att.getTeacherId());
                dto.setTeacherName(att.getTeacherName());
                dto.setDate(att.getDate());
                dto.setMonth(att.getMonth());
                dto.setInTime(att.getInTime());
                dto.setOutTime(att.getOutTime());
                dto.setMark(att.getMark());
                return dto;
            }).collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
        }
        return responseList;
    }


    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByTeacherIdAndMonth(Integer teacherId, String month) {
        List<TeachersAttendanceResponseDto> responseList = new ArrayList<>();

        try {
            if (teacherId == null || month == null || month.isEmpty()) {
                throw new IllegalArgumentException("Teacher ID and Month cannot be null or empty");
            }

            List<TeachersAttendance> attendances = attendanceRepo.findByTeacherIdAndMonth(teacherId, month.toUpperCase());

            if (attendances == null || attendances.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No attendance found for Teacher ID: " + teacherId + " in month: " + month);
            }

            responseList = attendances.stream().map(att -> {
                TeachersAttendanceResponseDto dto = new TeachersAttendanceResponseDto();
                dto.setAttendanceId(att.getTeachersAttendanceId());
                dto.setTeacherId(att.getTeacherId());
                dto.setTeacherName(att.getTeacherName());
                dto.setDate(att.getDate());
                dto.setMonth(att.getMonth());
                dto.setInTime(att.getInTime());
                dto.setOutTime(att.getOutTime());
                dto.setMark(att.getMark());
                return dto;
            }).collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.err.println("Data not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return responseList;
    }

    @Override
    public TeachersAttendance updateTeacherAttendance(Integer teachersAttendanceId, TeachersAttendance updatedAttendance) {

        TeachersAttendance existingAttendance = attendanceRepo.findById(teachersAttendanceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher Attendance not found with ID: " + teachersAttendanceId
                ));

        if (updatedAttendance.getTeacherName() != null) {
            existingAttendance.setTeacherName(updatedAttendance.getTeacherName());
        }
        if (updatedAttendance.getInTime() != null) {
            existingAttendance.setInTime(updatedAttendance.getInTime());
        }
        if (updatedAttendance.getOutTime() != null) {
            existingAttendance.setOutTime(updatedAttendance.getOutTime());
        }
        if (updatedAttendance.getDate() != null) {
            existingAttendance.setDate(updatedAttendance.getDate());
        }
        if (updatedAttendance.getMonth() != null) {
            existingAttendance.setMonth(updatedAttendance.getMonth());
        }
        if (updatedAttendance.getTeacherId() != null) {
            existingAttendance.setTeacherId(updatedAttendance.getTeacherId());
        }

        //  Recalculate mark if both inTime and outTime are present
        if (existingAttendance.getInTime() != null && existingAttendance.getOutTime() != null) {
            double hours = calculateHours(existingAttendance.getInTime(), existingAttendance.getOutTime());
            String mark = getMark(hours);
            existingAttendance.setMark(mark);
        }

        return attendanceRepo.save(existingAttendance);
    }

    @Override
    public void deleteTeacherAttendance(Integer teachersAttendanceId) {
        TeachersAttendance existingAttendance = attendanceRepo.findById(teachersAttendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + teachersAttendanceId));

        attendanceRepo.delete(existingAttendance);
    }

    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByDate(String date) {
        List<TeachersAttendanceResponseDto> responseList = new ArrayList<>();

        try {
            if (date == null || date.trim().isEmpty()) {
                throw new IllegalArgumentException("Date cannot be null or empty");
            }

            // Validate date format (dd-MM-yyyy)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate parsedDate;

            try {
                parsedDate = LocalDate.parse(date, formatter);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy");
            }

            String formattedDate = parsedDate.format(formatter); // same format as DB

            List<TeachersAttendance> attendances = attendanceRepo.findByDate(formattedDate);

            if (attendances == null || attendances.isEmpty()) {
                throw new ResourceNotFoundException("No attendance records found for date: " + date);
            }

            responseList = attendances.stream().map(att -> {
                TeachersAttendanceResponseDto dto = new TeachersAttendanceResponseDto();
                dto.setAttendanceId(att.getTeachersAttendanceId());
                dto.setTeacherId(att.getTeacherId());
                dto.setTeacherName(att.getTeacherName());
                dto.setDate(att.getDate());
                dto.setMonth(att.getMonth());
                dto.setInTime(att.getInTime());
                dto.setOutTime(att.getOutTime());
                dto.setMark(att.getMark());
                return dto;
            }).collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.err.println("Data Not Found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        }

        return responseList;
    }


    @Override
    public TeachersAttendanceSummaryDto getAttendanceSummaryByTeacherIdAndMonth(Integer teacherId, String month) {
        TeachersAttendanceSummaryDto summary = null;

        try {
            if (teacherId == null) {
                throw new IllegalArgumentException("Teacher ID cannot be null");
            }
            if (month == null || month.isEmpty()) {
                throw new IllegalArgumentException("Month cannot be null or empty");
            }

            // Fetch attendance only for the given teacher and month
            List<TeachersAttendance> attendances = attendanceRepo.findByTeacherIdAndMonth(teacherId, month.toUpperCase());

            if (attendances == null || attendances.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No attendance found for Teacher ID: " + teacherId + " in month: " + month);
            }

            long fullDays = attendances.stream()
                    .filter(a -> "FULL_DAY".equalsIgnoreCase(a.getMark()))
                    .count();

            long halfDays = attendances.stream()
                    .filter(a -> "HALF_DAY".equalsIgnoreCase(a.getMark()))
                    .count();

            long absentDays = attendances.stream()
                    .filter(a -> "ABSENT".equalsIgnoreCase(a.getMark()))
                    .count();

            long totalDays = attendances.size();

            double attendancePercentage = (double) ((fullDays + (halfDays * 0.5)) / totalDays) * 100;

            summary = new TeachersAttendanceSummaryDto();
            summary.setTeacherId(teacherId);
            summary.setTeacherName(attendances.get(0).getTeacherName());
            summary.setMonth(month.toUpperCase());
            summary.setTotalDays(totalDays);
            summary.setFullDays(fullDays);
            summary.setHalfDays(halfDays);
            summary.setAbsentDays(absentDays);
            summary.setAttendancePercentage(Math.round(attendancePercentage * 100.0) / 100.0);

        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.err.println("Data Not Found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        }

        return summary;
    }

    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByYear(String year) {
        List<TeachersAttendanceResponseDto> responseList = new ArrayList<>();

        try {
            if (year == null || year.trim().isEmpty()) {
                throw new IllegalArgumentException("Year cannot be null or empty");
            }

            List<TeachersAttendance> attendances = attendanceRepo.findAll();

            // Filter by year extracted from the date field (dd-MM-yyyy)
            List<TeachersAttendance> filtered = attendances.stream()
                    .filter(att -> {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            LocalDate date = LocalDate.parse(att.getDate(), formatter);
                            return date.getYear() == Integer.parseInt(year);
                        } catch (Exception e) {
                            return false; // Skip invalid date format
                        }
                    })
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                throw new ResourceNotFoundException("No attendance found for the year " + year);
            }

            responseList = filtered.stream().map(att -> {
                TeachersAttendanceResponseDto dto = new TeachersAttendanceResponseDto();
                dto.setAttendanceId(att.getTeachersAttendanceId());
                dto.setTeacherId(att.getTeacherId());
                dto.setTeacherName(att.getTeacherName());
                dto.setDate(att.getDate());
                dto.setMonth(att.getMonth());
                dto.setInTime(att.getInTime());
                dto.setOutTime(att.getOutTime());
                dto.setMark(att.getMark());
                return dto;
            }).collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.err.println("Data Not Found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        }

        return responseList;
    }
}


