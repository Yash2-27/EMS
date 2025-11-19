package com.spring.jwt.CEOServiceTest;

import com.spring.jwt.CEO.DashboredChart.Service.Impl.ChartServiceImpl;
import com.spring.jwt.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChartServiceImplTest {

    private StudentRepository studentRepository;
    private ChartServiceImpl chartService;

    @BeforeEach
    void setUp() {
        studentRepository = Mockito.mock(StudentRepository.class);
        chartService = new ChartServiceImpl(studentRepository);
    }

    // ----------------------------------------------------
    // TEST 1: PIE CHART
    // ----------------------------------------------------
    @Test
    void testGetPieChart() {

        List<Object[]> mockData = Arrays.asList(
                new Object[]{"11", 50L},
                new Object[]{"12", 40L}
        );

        when(studentRepository.countByClass()).thenReturn(mockData);

        Map<String, Long> result = chartService.getPieChart();

        assertEquals(2, result.size());
        assertEquals(50L, result.get("Students Class 11"));
        assertEquals(40L, result.get("Students Class 12"));
    }

    // ----------------------------------------------------
    // TEST 2: BAR CHART
    // ----------------------------------------------------
    @Test
    void testGetBarChart() {

        List<Object[]> mockData = Arrays.asList(
                new Object[]{1, "11", 86},
                new Object[]{2, "11", 80},
                new Object[]{1, "12", 85},
                new Object[]{2, "12", 75}
        );

        when(studentRepository.getMonthlyCount()).thenReturn(mockData);

        Map<String, List<Integer>> result = chartService.getBarChart();

        // Validate keys exist
        assertTrue(result.containsKey("11th"));
        assertTrue(result.containsKey("12th"));

        // Validate month values
        assertEquals(86, result.get("11th").get(0));
        assertEquals(80, result.get("11th").get(1));

        assertEquals(85, result.get("12th").get(0));
        assertEquals(75, result.get("12th").get(1));

        // Remaining months should be zero
        for (int i = 2; i < 12; i++) {
            assertEquals(0, result.get("11th").get(i));
            assertEquals(0, result.get("12th").get(i));
        }
    }

    // ----------------------------------------------------
    // TEST 3: Should handle empty DB properly
    // ----------------------------------------------------
    @Test
    void testGetBarChart_EmptyData() {

        when(studentRepository.getMonthlyCount()).thenReturn(Collections.emptyList());

        Map<String, List<Integer>> result = chartService.getBarChart();

        assertEquals(12, result.get("11th").size());
        assertEquals(12, result.get("12th").size());

        assertTrue(result.get("11th").stream().allMatch(v -> v == 0));
        assertTrue(result.get("12th").stream().allMatch(v -> v == 0));
    }
}
