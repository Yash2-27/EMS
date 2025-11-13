//package com.spring.jwt.TeachersAttendanceTest;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Optional;
//import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceDto;
//import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceResponseDto;
//import com.spring.jwt.TeachersAttendance.dto.TeachersAttendanceSummaryDto;
//import com.spring.jwt.TeachersAttendance.entity.TeachersAttendance;
//import com.spring.jwt.TeachersAttendance.repository.TeachersAttendanceRepository;
//import com.spring.jwt.entity.Teacher;
//import com.spring.jwt.repository.TeacherRepository;
//import com.spring.jwt.exception.ResourceNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import java.util.List;
//class TeachersAttendanceServiceTest {
//
//    @Mock
//    private TeachersAttendanceRepository attendanceRepo;
//
//    @Mock
//    private TeacherRepository teacherRepo;
//
//    @InjectMocks
//    private com.spring.jwt.TeachersAttendance.serviceImpl.TeachersAttendanceServiceImpl service;
//
//    private TeachersAttendance attendance;
//    private TeachersAttendance updatedAttendance;
//    private Teacher teacher;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        teacher = new Teacher();
//        teacher.setTeacherId(1);
//        teacher.setName("John Doe");
//
//        attendance = new TeachersAttendance();
//        attendance.setTeachersAttendanceId(1);
//        attendance.setTeacherId(1);
//        attendance.setTeacherName("John Doe");
//        attendance.setDate("09-10-2025");
//        attendance.setMonth("OCTOBER");
//        attendance.setInTime("09:00:00");
//        attendance.setOutTime("17:00:00");
//        attendance.setMark("FULL_DAY");
//
//        updatedAttendance = new TeachersAttendance();
//        updatedAttendance.setTeacherId(1);
//        updatedAttendance.setTeacherName("Jane Doe");
//        updatedAttendance.setDate("09-10-2025");
//        updatedAttendance.setMonth("OCTOBER");
//        updatedAttendance.setInTime("10:00:00");
//        updatedAttendance.setOutTime("18:00:00");
//        updatedAttendance.setMark("FULL_DAY");
//    }
//
//    // =================== CREATE ===================
//    @Test
//    void testCreateAttendance_Success() {
//        // Arrange
//        TeachersAttendanceDto dto = new TeachersAttendanceDto();
//        dto.setTeacherId(1); Teacher teacher = new Teacher();
//        teacher.setTeacherId(1);
//        teacher.setName("John Doe");
//
//        when(teacherRepo.findById(1)).thenReturn(Optional.of(teacher));
//        when(attendanceRepo.findByTeacherIdAndDate(anyInt(), anyString())).thenReturn(Optional.empty());
//        when(attendanceRepo.save(any())).thenAnswer(i -> i.getArguments()[0]);
//        TeachersAttendanceResponseDto response = service.createAttendance(dto);
//        assertNotNull(response);
//        assertEquals("John Doe", response.getTeacherName());
//    }
//
//    // =================== READ ===================
//    @Test
//    void testGetAttendanceByTeacherId_Success() {
//        when(attendanceRepo.findByTeacherId(1)).thenReturn(Arrays.asList(attendance));
//
//        List<TeachersAttendanceResponseDto> response = service.getAttendanceByTeacherId(1);
//
//        assertNotNull(response);
//        assertEquals(1, response.size());
//        assertEquals("John Doe", response.get(0).getTeacherName());
//    }
//
//    @Test
//    void testGetAttendanceByTeacherId_NotFound() {
//        when(attendanceRepo.findByTeacherId(1)).thenReturn(Collections.emptyList());
//        assertThrows(ResourceNotFoundException.class, () -> service.getAttendanceByTeacherId(1));
//    }
//
//    // =================== UPDATE ===================
////    @Test
////    void testUpdateTeacherAttendance_Success() {
////        when(attendanceRepo.findById(1)).thenReturn(Optional.of(attendance));
////        when(attendanceRepo.save(any(TeachersAttendance.class))).thenAnswer(i -> i.getArguments()[0]);
////
////        TeachersAttendance result = service.updateTeacherAttendance(1, updatedAttendance);
////
////        assertNotNull(result);
////        assertEquals("Jane Doe", result.getTeacherName());
////    }
//
//    @Test
//    void testUpdateTeacherAttendance_NotFound() {
//        when(attendanceRepo.findById(1)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () -> service.updateTeacherAttendance(1, updatedAttendance));
//    }
//
//    // =================== DELETE ===================
//    @Test
//    void testDeleteTeacherAttendance_Success() {
//        when(attendanceRepo.findById(1)).thenReturn(Optional.of(attendance));
//        doNothing().when(attendanceRepo).delete(attendance);
//
//        assertDoesNotThrow(() -> service.deleteTeacherAttendance(1));
//        verify(attendanceRepo, times(1)).delete(attendance);
//    }
//
//    @Test
//    void testDeleteTeacherAttendance_NotFound() {
//        when(attendanceRepo.findById(1)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () -> service.deleteTeacherAttendance(1));
//        verify(attendanceRepo, never()).delete(any());
//    }
//
//    // =================== SUMMARY ===================
//    @Test
//    void testGetAttendanceSummaryByTeacherId_Success() {
//        when(attendanceRepo.findByTeacherId(1)).thenReturn(Arrays.asList(attendance));
//
//        TeachersAttendanceSummaryDto summary = service.getAttendanceSummaryByTeacherId(1);
//
//        assertNotNull(summary);
//        assertEquals(1, summary.getTotalDays());
//        assertEquals(100.0, summary.getAttendancePercentage());
//    }
//
//    @Test
//    void testGetAttendanceSummaryByTeacherId_NotFound() {
//        when(attendanceRepo.findByTeacherId(1)).thenReturn(Collections.emptyList());
//        assertThrows(ResourceNotFoundException.class, () -> service.getAttendanceSummaryByTeacherId(1));
//    }
//
//    // =================== GET BY DATE ===================
//    @Test
//    void testGetAttendanceByDate_Success() {
//        when(attendanceRepo.findByDate("09-10-2025")).thenReturn(Arrays.asList(attendance));
//
//        List<TeachersAttendanceResponseDto> response = service.getAttendanceByDate("09-10-2025");
//
//        assertNotNull(response);
//        assertEquals(1, response.size());
//    }
//
//    @Test
//    void testGetAttendanceByDate_NotFound() {
//        when(attendanceRepo.findByDate("09-10-2025")).thenReturn(Collections.emptyList());
//        assertThrows(ResourceNotFoundException.class, () -> service.getAttendanceByDate("09-10-2025"));
//    }
//}
