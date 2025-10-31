package com.spring.jwt.TeachersTest;

import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.Teachers.exception.DropdownResourceNotFoundException;
import com.spring.jwt.Teachers.service.impl.DropdownServiceImpl;
import com.spring.jwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DropdownServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private DropdownServiceImpl dropdownService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ 1. Test getAllClasses() - Success
    @Test
    void getAllClasses_ShouldReturnListOfClasses() {
        when(teacherRepository.findAllClass()).thenReturn(Arrays.asList("10th", "12th"));

        List<String> result = dropdownService.getAllClasses();

        assertEquals(2, result.size());
        assertEquals("10th", result.get(0));
        verify(teacherRepository, times(1)).findAllClass();
    }

    // ❌ 2. Test getAllClasses() - Empty Result
    @Test
    void getAllClasses_ShouldThrowIfEmpty() {
        when(teacherRepository.findAllClass()).thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class, () -> dropdownService.getAllClasses());
    }

    // ✅ 3. Test getTeachersByClasses() - Success
    @Test
    void getTeachersByClasses_ShouldReturnTeachers() {
        when(teacherRepository.findTeachersByClasses("10th")).thenReturn(Arrays.asList("Mr. A", "Ms. B"));

        List<Object> result = dropdownService.getTeachersByClasses("10th");

        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findTeachersByClasses("10th");
    }

    // ❌ 4. Test getTeachersByClasses() - Empty List
    @Test
    void getTeachersByClasses_ShouldThrowIfEmpty() {
        when(teacherRepository.findTeachersByClasses("10th")).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> dropdownService.getTeachersByClasses("10th"));
    }

    // ✅ 5. Test getSubjects() - Success
    @Test
    void getSubjects_ShouldReturnSubjects() {
        when(teacherRepository.findTeachersBySubject("10th", 1))
                .thenReturn(Arrays.asList("Physics", "Science"));

        List<String> result = dropdownService.getSubjects("10th", 1);

        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findTeachersBySubject("10th", 1);
    }

    // ❌ 6. Test getSubjects() - Empty List
    @Test
    void getSubjects_ShouldThrowIfEmpty() {
        when(teacherRepository.findTeachersBySubject("10th", 1)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () ->
                dropdownService.getSubjects("10th", 1));
    }

    // ✅ 7. Test getQuestionPaper() - Success
    @Test
    void getQuestionPaper_ShouldReturnQuestions() {
        TeacherQuestionFlatDto dto = new TeacherQuestionFlatDto(
                "Physics Test 1", "What is 2+2?", "1", "2", "3", "4",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0)
        );

        when(teacherRepository.findByQuestionPaper("10th", 1, "Physics"))
                .thenReturn(List.of(dto));

        List<TeacherQuestionFlatDto> result =
                dropdownService.getQuestionPaper("10th", 1, "Physics");

        assertEquals(1, result.size());
        assertEquals("Physics Test 1", result.get(0).getTitle());
    }

    // ❌ 8. Test getQuestionPaper() - Empty List

    @Test
    void getQuestionPaper_ShouldThrowIfEmpty() {
        when(teacherRepository.findByQuestionPaper("10th", 1, "Physics"))
                .thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () ->
                dropdownService.getQuestionPaper("10th", 1, "Physics"));
    }
}
