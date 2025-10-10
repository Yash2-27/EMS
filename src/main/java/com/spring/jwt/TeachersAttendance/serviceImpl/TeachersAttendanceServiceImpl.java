package com.spring.jwt.TeachersAttendance.serviceImpl;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceResponseDto;
import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceSummaryDto;
import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
import com.spring.jwt.TeachersAttendance.service.TeachersAttendanceService;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        if (dto.getTeacherId() == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null");
        }

        // Get teacher info from DB
        Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher not found with ID: " + dto.getTeacherId()
                ));

        //  Format Date in dd-MM-yyyy (Asia/Kolkata timezone)
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(dateFormatter);

        //  Current time in Asia/Kolkata
        LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(timeFormatter);

        // Check if attendance exists today
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

        // Set inTime if not set
        if (attendance.getInTime() == null) {
            attendance.setInTime(formattedTime);
        }
        // Always update last outTime
        attendance.setOutTime(formattedTime);

        // Calculate hours
        double hours = calculateHours(attendance.getInTime(), attendance.getOutTime());

        // Mark attendance with 15–20 min grace
        attendance.setMark(getMark(hours));

        // Save attendance
        TeachersAttendance saved = attendanceRepo.save(attendance);

        // Map to response DTO
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
    //    @Override
//    public TeachersAttendanceResponseDto createAttendance(TeachersAttendanceDto dto) {
//        try {
//            // Validate teacher ID
//            if (dto.getTeacherId() == null) {
//                throw new IllegalArgumentException("Teacher ID must not be null");
//            }
//            // Get today's date
//            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            // Fetch existing attendance for today
//            TeachersAttendance attendance = attendanceRepo.findByTeacherIdAndDate(dto.getTeacherId(), today)
//                    .orElseGet(() -> {
//                        TeachersAttendance a = new TeachersAttendance();
//                        a.setTeacherId(dto.getTeacherId());
//                        // Fetch real teacher name from Teacher table
//                        Teacher teacher = teacherRepo.findById(dto.getTeacherId())
//                                .orElseThrow(() -> new RuntimeException(
//                                        "Teacher not found with id: " + dto.getTeacherId()));
//                        a.setTeacherName(teacher.getName());
//                        a.setDate(today);
//                        a.setMonth(LocalDate.now().getMonth().name());
//                        return a;
//                    });
//            // Current punch time
//            String nowTime = LocalTime.now().format(timeFormatter);
//            // Set first inTime if null
//            if (attendance.getInTime() == null) {
//                attendance.setInTime(nowTime);
//            }
//            // Always update last outTime
//            attendance.setOutTime(nowTime);
//            // Calculate hours
//            double hours = calculateHours(attendance.getInTime(), attendance.getOutTime());
//            // Set mark with 15–20 min grace
//            attendance.setMark(getMark(hours));
//            // Save to DB
//            attendanceRepo.save(attendance);
//            // Map to response DTO
//            TeachersAttendanceResponseDto response = new TeachersAttendanceResponseDto();
//            response.setAttendanceId(attendance.getTeachersAttendanceId());
//            response.setTeacherId(attendance.getTeacherId());
//            response.setTeacherName(attendance.getTeacherName());
//            response.setDate(attendance.getDate());
//            response.setMonth(attendance.getMonth());
//            response.setInTime(attendance.getInTime());
//            response.setOutTime(attendance.getOutTime());
//            response.setMark(attendance.getMark());
//            return response;
//        } catch (IllegalArgumentException e) {
//            throw new RuntimeException("Invalid request: " + e.getMessage(), e);
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Error while creating attendance: " + e.getMessage(), e);
//        } catch (Exception e) {
//            throw new RuntimeException("Unexpected error occurred while saving attendance", e);
//        }
//    }
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
        if (teacherId == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null");
        }
        List<TeachersAttendance> attendances = attendanceRepo.findByTeacherId(teacherId);
        if (attendances == null || attendances.isEmpty()) {
            throw new ResourceNotFoundException("No attendance found for Teacher ID: " + teacherId);
        }
        return attendances.stream().map(att -> {
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
    }

    @Override
    public TeachersAttendance updateTeacherAttendance(Integer teachersAttendanceId, TeachersAttendance updatedAttendance) {
        // Find existing attendance record
        TeachersAttendance existingAttendance = attendanceRepo.findById(teachersAttendanceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Teacher Attendance not found with ID: " + teachersAttendanceId
                ));
        // Update fields
        existingAttendance.setTeacherName(updatedAttendance.getTeacherName());
        existingAttendance.setInTime(updatedAttendance.getInTime());
        existingAttendance.setOutTime(updatedAttendance.getOutTime());
        existingAttendance.setMark(updatedAttendance.getMark());
        existingAttendance.setDate(updatedAttendance.getDate());
        existingAttendance.setMonth(updatedAttendance.getMonth());
        existingAttendance.setTeacherId(updatedAttendance.getTeacherId());
        // Save updated record
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
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }

        // Ensure date is in dd/MM/yyyy format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use dd/MM/yyyy");
        }

        String formattedDate = parsedDate.format(formatter); // same format as DB

        List<TeachersAttendance> attendances = attendanceRepo.findByDate(formattedDate);

        if (attendances == null || attendances.isEmpty()) {
            throw new ResourceNotFoundException("No attendance records found for date: " + date);
        }

        return attendances.stream().map(att -> {
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
    }


    @Override
    public List<TeachersAttendanceResponseDto> getAttendanceByMonth(String month, String year) {
        if (month == null || year == null) {
            throw new IllegalArgumentException("Month and Year cannot be null");
        }
        List<TeachersAttendance> attendances = attendanceRepo.findAll();
        // Filter by month and year extracted from the date field (yyyy-MM-dd)
        List<TeachersAttendance> filtered = attendances.stream()
                .filter(att -> {
                    try {
//                        LocalDate date = LocalDate.parse(att.getDate()); // Parse date string
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDate date = LocalDate.parse(att.getDate(), formatter);
                        return date.getMonthValue() == Integer.parseInt(month)
                                && date.getYear() == Integer.parseInt(year);
                    } catch (Exception e) {
                        return false; // Skip invalid date format rows
                    }
                })
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new ResourceNotFoundException("No attendance found for month " + month + "/" + year);
        }
        return filtered.stream().map(att -> {
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
    }

    @Override
    public TeachersAttendanceSummaryDto getAttendanceSummaryByTeacherId(Integer teacherId) {
        if (teacherId == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null");
        }
        List<TeachersAttendance> attendances = attendanceRepo.findByTeacherId(teacherId);

        if (attendances == null || attendances.isEmpty()) {
            throw new ResourceNotFoundException("No attendance found for Teacher ID: " + teacherId);
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

        TeachersAttendanceSummaryDto summary = new TeachersAttendanceSummaryDto();
        summary.setTeacherId(teacherId);
        summary.setTeacherName(attendances.get(0).getTeacherName());
        summary.setTotalDays(totalDays);
        summary.setFullDays(fullDays);
        summary.setHalfDays(halfDays);
        summary.setAbsentDays(absentDays);
        summary.setAttendancePercentage(Math.round(attendancePercentage * 100.0) / 100.0);

        return summary;
    }
}


