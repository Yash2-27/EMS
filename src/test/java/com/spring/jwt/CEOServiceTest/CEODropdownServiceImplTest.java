package com.spring.jwt.CEOServiceTest;

import com.spring.jwt.CEO.DashboredChart.Service.Impl.CEODropdownServiceImpl;
import com.spring.jwt.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CEODropdownServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CEODropdownServiceImpl dropdownService;

    // -----------------------------
    // getStudentClass() Tests
    // -----------------------------
    @Test
    void testGetStudentClass_ReturnsList() {

        List<String> mockClasses = Arrays.asList("11", "12");
        when(studentRepository.findDistinctStudentClasses()).thenReturn(mockClasses);

        List<String> result = dropdownService.getStudentClass();

        assertEquals(2, result.size());
        assertEquals("11", result.get(0));
        assertEquals("12", result.get(1));
    }

    @Test
    void testGetStudentClass_EmptyList() {

        when(studentRepository.findDistinctStudentClasses())
                .thenReturn(Collections.emptyList());

        List<String> result = dropdownService.getStudentClass();

        assertEquals(0, result.size());
    }

    // -----------------------------
    // getStudentBatch() Tests
    // -----------------------------
    @Test
    void testGetStudentBatch_ReturnsList() {

        List<String> mockBatches = Arrays.asList("2023", "2024");
        when(studentRepository.findByStudentBatch()).thenReturn(mockBatches);

        List<String> result = dropdownService.getStudentBatch();

        assertEquals(2, result.size());
        assertEquals("2023", result.get(0));
        assertEquals("2024", result.get(1));
    }

    @Test
    void testGetStudentBatch_EmptyList() {

        when(studentRepository.findByStudentBatch()).thenReturn(Collections.emptyList());

        List<String> result = dropdownService.getStudentBatch();

        assertEquals(0, result.size());
    }
}
