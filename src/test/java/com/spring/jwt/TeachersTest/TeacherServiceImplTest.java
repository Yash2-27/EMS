package com.spring.jwt.TeachersTest;

import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import com.spring.jwt.Teachers.service.impl.TeacherServiceImpl;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.PapersAndTeacherException;
import com.spring.jwt.repository.TeacherRepository;
import com.spring.jwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ 1. Test getAllTeachers() - Success
    @Test
    void getAllTeachers_ShouldReturnTeacherInfoList() {
        // Arrange (fake data)
        Teacher teacher1 = new Teacher();
        teacher1.setTeacherId(1);
        teacher1.setName("John");
        teacher1.setSub("Math");
        teacher1.setDeg("M.Sc");
        teacher1.setStatus("Active");
        teacher1.setUserId(101);

        User user1 = new User();
        user1.setId(101);
        user1.setEmail("john@example.com");
        user1.setMobileNumber(9876543210L);

        when(teacherRepository.findAll()).thenReturn(List.of(teacher1));
        when(userRepository.findById(101L)).thenReturn(Optional.of(user1));

        // Act
        List<TeacherInfoDto> result = teacherService.getAllTeachers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals(9876543210L, result.get(0).getMobileNumber());

        verify(teacherRepository, times(1)).findAll();
        verify(userRepository, times(1)).findById(101L);
    }

    // ✅ 2. Test getAllTeachers() - No Users Found (email null)
    @Test
    void getAllTeachers_ShouldHandleMissingUser() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);
        teacher.setName("Alice");
        teacher.setSub("Physics");
        teacher.setUserId(202);

        when(teacherRepository.findAll()).thenReturn(List.of(teacher));
        when(userRepository.findById(202L)).thenReturn(Optional.empty());

        List<TeacherInfoDto> result = teacherService.getAllTeachers();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertNull(result.get(0).getEmail());
        assertNull(result.get(0).getMobileNumber());
    }

    // ✅ 3. Test getAllTeachers() - Empty List (Exception Case)
    @Test
    void getAllTeachers_ShouldThrowExceptionIfEmpty() {
        when(teacherRepository.findAll()).thenReturn(List.of());

        PapersAndTeacherException exception = assertThrows(
                PapersAndTeacherException.class,
                () -> teacherService.getAllTeachers()
        );

        assertEquals("No teachers found in the database.", exception.getMessage());
        verify(teacherRepository, times(1)).findAll();
        verifyNoInteractions(userRepository);
    }

    // ✅ 4. Test findById() - Success
    @Test
    void findById_ShouldReturnTeacherInfo() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        teacher.setName("John");
        teacher.setSub("Chemistry");
        teacher.setDeg("Ph.D");
        teacher.setStatus("Active");
        teacher.setUserId(101);

        User user = new User();
        user.setId(101);
        user.setEmail("john@school.com");
        user.setMobileNumber(9876543210L);

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(userRepository.findById(101L)).thenReturn(Optional.of(user));

        TeacherInfoDto result = teacherService.findById(1);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("Chemistry", result.getSub());
        assertEquals("john@school.com", result.getEmail());
        assertEquals(9876543210L, result.getMobileNumber());
    }

    // ✅ 5. Test findById() - Not Found (Exception)
    @Test
    void findById_ShouldThrowExceptionIfTeacherNotFound() {
        when(teacherRepository.findById(999)).thenReturn(Optional.empty());

        PapersAndTeacherException exception = assertThrows(
                PapersAndTeacherException.class,
                () -> teacherService.findById(999)
        );

        assertEquals("Teacher not found with id: 999", exception.getMessage());
        verify(teacherRepository, times(1)).findById(999);
        verifyNoInteractions(userRepository);
    }
}
