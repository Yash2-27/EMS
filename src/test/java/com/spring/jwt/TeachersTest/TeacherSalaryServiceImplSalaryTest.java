//package com.spring.jwt.TeachersTest;
//
//import com.spring.jwt.TeachersAttendance.dto.TeacherSalaryResponseDto;
//import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
//import com.spring.jwt.TeachersAttendance.repository.TeacherSalaryRepository;
//import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
//import com.spring.jwt.TeachersAttendance.serviceImpl.TeacherSalaryServiceImpl;
//import com.spring.jwt.exception.ResourceNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TeacherSalaryServiceImplSalaryTest {
//
//    @Mock
//    private TeachersAttendanceRepository attendanceRepo;
//
//    @Mock
//    private TeacherSalaryRepository teacherSalaryRepo;
//
//    @InjectMocks
//    private TeacherSalaryServiceImpl teacherSalaryService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCalculateSalary_Success() {
//        Integer teacherId = 10000;
//        String month = "October";
//        Integer year = 2025;
//
//        // Mock attendance data using setters
//        List<TeachersAttendance> attendances = new ArrayList<>();
//
//        TeachersAttendance a1 = new TeachersAttendance();
//        a1.setTeachersAttendanceId(1);
//        a1.setTeacherId(teacherId);
//        a1.setTeacherName("John Mathew");
//        a1.setDate("2025-10-01");
//        a1.setMark("ABSENT");
//        attendances.add(a1);
//
//        TeachersAttendance a2 = new TeachersAttendance();
//        a2.setTeachersAttendanceId(2);
//        a2.setTeacherId(teacherId);
//        a2.setTeacherName("John Mathew");
//        a2.setDate("2025-10-02");
//        a2.setMark("PRESENT");
//        attendances.add(a2);
//
//        TeachersAttendance a3 = new TeachersAttendance();
//        a3.setTeachersAttendanceId(3);
//        a3.setTeacherId(teacherId);
//        a3.setTeacherName("John Mathew");
//        a3.setDate("2025-10-03");
//        a3.setMark("HALF_DAY");
//        attendances.add(a3);
//
//        TeachersAttendance a4 = new TeachersAttendance();
//        a4.setTeachersAttendanceId(4);
//        a4.setTeacherId(teacherId);
//        a4.setTeacherName("John Mathew");
//        a4.setDate("2025-10-05");
//        a4.setMark("LATE");
//        attendances.add(a4);
//
//        // Mock repository methods
//        when(attendanceRepo.findByTeacherIdAndMonth(teacherId, month)).thenReturn(attendances);
//        when(teacherSalaryRepo.findByTeacherIdAndMonthAndYear(Long.valueOf(teacherId), month, year)).thenReturn(1200);
//
//        // Call service
//        TeacherSalaryResponseDto response = teacherSalaryService.calculateSalary(teacherId, month, year);
//
//        // Verify response
//        assertNotNull(response);
//        assertEquals(teacherId, response.getTeacherId());
//        assertEquals("October", response.getMonth());
//        assertEquals(2025, response.getYear());
//        assertEquals(1200.0, response.getPerDaySalary());
//        // Expected = 0 (absent) + 1200 (present) + 600 (half) + 900 (late) = 2700
//        assertEquals(2700.0, response.getTotalSalary());
//        assertEquals(4, response.getAttendanceMap().size());
//
//        // Verify repository interactions
//        verify(attendanceRepo, times(1)).findByTeacherIdAndMonth(teacherId, month);
//        verify(teacherSalaryRepo, times(1)).findPerDaySalary(teacherId, month, year);
//    }
//
//    @Test
//    void testCalculateSalary_NoAttendanceFound() {
//        Integer teacherId = 101;
//        String month = "October";
//        Integer year = 2025;
//
//        when(attendanceRepo.findByTeacherIdAndMonth(teacherId, month)).thenReturn(Collections.emptyList());
//
//        assertThrows(ResourceNotFoundException.class, () ->
//                teacherSalaryService.calculateSalary(teacherId, month, year));
//    }
//
//    @Test
//    void testCalculateSalary_NoSalaryRecordFound() {
//        Integer teacherId = 102;
//        String month = "October";
//        Integer year = 2025;
//
//        TeachersAttendance attendance = new TeachersAttendance();
//        attendance.setTeachersAttendanceId(1);
//        attendance.setTeacherId(teacherId);
//        attendance.setTeacherName("Mark");
//        attendance.setDate("2025-10-01");
//        attendance.setMark("PRESENT");
//
//        List<TeachersAttendance> attendances = List.of(attendance);
//
//        when(attendanceRepo.findByTeacherIdAndMonth(teacherId, month)).thenReturn(attendances);
//        when(teacherSalaryRepo.findByTeacherIdAndMonthAndYear(Long.valueOf(teacherId), month, year)).thenReturn(null);
//
//        assertThrows(ResourceNotFoundException.class, () ->
//                teacherSalaryService.calculateSalary(teacherId, month, year));
//    }
//
//    @Test
//    void testCalculateSalary_NullInputs() {
//        assertThrows(IllegalArgumentException.class, () -> teacherSalaryService.calculateSalary(null, "October", 2025));
//        assertThrows(IllegalArgumentException.class, () -> teacherSalaryService.calculateSalary(100, " ", 2025));
//        assertThrows(IllegalArgumentException.class, () -> teacherSalaryService.calculateSalary(100, "October", 0));
//    }
//}
