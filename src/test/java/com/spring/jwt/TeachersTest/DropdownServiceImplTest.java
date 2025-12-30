package com.spring.jwt.TeachersTest;

import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.exception.DropdownResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
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

    // ===================== getAllClasses =====================
    @Test
    void getAllClasses_ShouldReturnListOfClasses() {
        when(teacherRepository.findAllClass()).thenReturn(Arrays.asList("10th", "12th"));

        List<String> result = dropdownService.getAllClasses();

        assertEquals(2, result.size());
        assertEquals("10th", result.get(0));
        verify(teacherRepository, times(1)).findAllClass();
    }

    @Test
    void getAllClasses_ShouldThrowIfEmpty() {
        when(teacherRepository.findAllClass()).thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class, () -> dropdownService.getAllClasses());
    }

    // ===================== getTeachersByClasses =====================
    @Test
    void getTeachersByClasses_ShouldReturnTeachers() {
        when(teacherRepository.findTeachersByClasses("10th")).thenReturn(Arrays.asList("Mr. A", "Ms. B"));

        List<Object> result = dropdownService.getTeachersByClasses("10th");

        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findTeachersByClasses("10th");
    }

    @Test
    void getTeachersByClasses_ShouldThrowIfEmpty() {
        when(teacherRepository.findTeachersByClasses("10th")).thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class,
                () -> dropdownService.getTeachersByClasses("10th"));
    }

    // ===================== getSubjects =====================
    @Test
    void getSubjects_ShouldReturnSubjects() {
        when(teacherRepository.findTeachersBySubject("10th", 1))
                .thenReturn(Arrays.asList("Physics", "Science"));

        List<String> result = dropdownService.getSubjects("10th", 1);

        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findTeachersBySubject("10th", 1);
    }

    @Test
    void getSubjects_ShouldThrowIfEmpty() {
        when(teacherRepository.findTeachersBySubject("10th", 1)).thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class,
                () -> dropdownService.getSubjects("10th", 1));
    }

    // ===================== getQuestionPaper =====================
    @Test
    void getQuestionPaper_ShouldReturnQuestions() {
        TeacherQuestionFlatDto dto = new TeacherQuestionFlatDto(
                "Physics Test 1", "What is 2+2?", "1", "2", "3", "4",
                false, LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0)
        );

        when(teacherRepository.findQuestionPaperBySubjectClassIsLive("Physics", "10th", true))
                .thenReturn(List.of(dto));

        List<TeacherQuestionFlatDto> result = dropdownService.getQuestionPaper("Physics", "10th", "Live");

        assertEquals(1, result.size());
        assertEquals("Physics Test 1", result.get(0).getTitle());
    }

    @Test
    void getQuestionPaper_ShouldThrowIfEmpty() {
        when(teacherRepository.findQuestionPaperBySubjectClassIsLive("Physics", "10th", true))
                .thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class,
                () -> dropdownService.getQuestionPaper("Physics", "10th", "Live"));
    }

    // ===================== getAllQuestionPaper =====================
    @Test
    void getAllQuestionPaper_ShouldReturnAll() {
        TeacherQuestionFlatDto dto1 = new TeacherQuestionFlatDto("Test1", "Q1", "A", "B", "C", "D", false,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TeacherQuestionFlatDto dto2 = new TeacherQuestionFlatDto("Test2", "Q2", "A", "B", "C", "D", false,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(teacherRepository.getAllQuestionPaper()).thenReturn(Arrays.asList(dto1, dto2));

        List<TeacherQuestionFlatDto> result = dropdownService.getAllQuestionPaper();

        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).getAllQuestionPaper();
    }

    // ===================== getQuestionBank =====================
    @Test
    void getQuestionBank_ShouldReturnQuestions() {
        TeacherQuestionFlatDto dto = new TeacherQuestionFlatDto(
                "Math Test", "Q1", "1", "2", "3", "4",
                false, LocalDateTime.now(), LocalDateTime.now().plusHours(1)
        );

        when(teacherRepository.findByQuestionBank("10th", 1, "Maths"))
                .thenReturn(List.of(dto));

        List<TeacherQuestionFlatDto> result = dropdownService.getQuestionBank("10th", 1, "Maths");

        assertEquals(1, result.size());
        verify(teacherRepository, times(1)).findByQuestionBank("10th", 1, "Maths");
    }

    @Test
    void getQuestionBank_ShouldThrowIfEmpty() {
        when(teacherRepository.findByQuestionBank("10th", 1, "Maths"))
                .thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class,
                () -> dropdownService.getQuestionBank("10th", 1, "Maths"));
    }

    // ===================== getAllQuestionBank =====================
    @Test
    void getAllQuestionBank_ShouldReturnAll() {
        TeacherQuestionFlatDto dto1 = new TeacherQuestionFlatDto("Test1", "Q1", "A", "B", "C", "D", false,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TeacherQuestionFlatDto dto2 = new TeacherQuestionFlatDto("Test2", "Q2", "A", "B", "C", "D", false,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(teacherRepository.getAllQuestionBank()).thenReturn(Arrays.asList(dto1, dto2));

        List<TeacherQuestionFlatDto> result = dropdownService.getAllQuestionBank();

        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).getAllQuestionBank();
    }

    // ===================== getAllSubjects =====================
    @Test
    void getAllSubjects_ShouldReturnSubjects() {
        when(teacherRepository.findAllSubjects()).thenReturn(Arrays.asList("Maths", "Science"));

        List<String> result = dropdownService.getAllSubjects();

        assertEquals(2, result.size());
        assertEquals("Maths", result.get(0));
        verify(teacherRepository, times(1)).findAllSubjects();
    }

    @Test
    void getAllSubjects_ShouldThrowIfEmpty() {
        when(teacherRepository.findAllSubjects()).thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class,
                () -> dropdownService.getAllSubjects());
    }

    // ===================== getClassesBySubject =====================
    @Test
    void getClassesBySubject_ShouldReturnClasses() {
        when(teacherRepository.findClassesBySubject("Maths")).thenReturn(Arrays.asList("10th", "12th"));

        List<String> result = dropdownService.getClassesBySubject("Maths");

        assertEquals(2, result.size());
        assertEquals("10th", result.get(0));
        verify(teacherRepository, times(1)).findClassesBySubject("Maths");
    }

    @Test
    void getClassesBySubject_ShouldThrowIfEmpty() {
        when(teacherRepository.findClassesBySubject("Maths")).thenReturn(Collections.emptyList());

        assertThrows(DropdownResourceNotFoundException.class,
                () -> dropdownService.getClassesBySubject("Maths"));
    }

    // ===================== getIsLiveOptions =====================
    @Test
    void getIsLiveOptions_ShouldReturnMappedList() {
        when(teacherRepository.findIsLiveByClassAndSubject("10th", "Maths"))
                .thenReturn(Arrays.asList(true, false, true));

        List<String> result = dropdownService.getIsLiveOptions("Maths", "10th");

        assertEquals(Arrays.asList("Live", "Past", "Live"), result);
    }
}
