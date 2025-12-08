package com.spring.jwt.CEOServiceTest;

import com.spring.jwt.CEO.CEOServiceImpl;
import com.spring.jwt.exception.MonthlyChartCustomException;
import com.spring.jwt.exception.PieChartCustomException;
import com.spring.jwt.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChartServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CEOServiceImpl chartService;

    // -------------------------------------------------------------
    // PIE CHART TESTS
    // -------------------------------------------------------------
    @Test
    void testGetPieChart_Success() {

        List<Object[]> mockRows = Arrays.asList(
                new Object[]{"11", 50L},
                new Object[]{"12", 30L}
        );

        when(studentRepository.countByClass()).thenReturn(mockRows);

        Map<String, Long> result = chartService.getPieChart();

        assertEquals(2, result.size());
        assertEquals(50L, result.get("Students Class 11"));
        assertEquals(30L, result.get("Students Class 12"));
    }

    @Test
    void testGetPieChart_EmptyList() {
        when(studentRepository.countByClass()).thenReturn(Collections.emptyList());

        assertThrows(PieChartCustomException.class,
                () -> chartService.getPieChart());
    }

    @Test
    void testGetPieChart_NullData() {
        when(studentRepository.countByClass()).thenReturn(null);

        assertThrows(PieChartCustomException.class,
                () -> chartService.getPieChart());
    }

    // -------------------------------------------------------------
    // MONTHLY CHART TESTS
    // -------------------------------------------------------------
    @Test
    void testGetMonthlyChart_Success() {

        List<Object[]> mockRows = Arrays.asList(
                new Object[]{1, "11", 80},   // Jan
                new Object[]{2, "11", 75},   // Feb
                new Object[]{1, "12", 90}    // Jan
        );

        when(studentRepository.getMonthlyCountFiltered("11", "2024"))
                .thenReturn(mockRows);

        Map<String, Map<String, Integer>> result =
                chartService.getMonthlyChart("11", "2024");

        assertEquals(2, result.size()); // 11 & 12 classes

        assertEquals(2, result.get("11").size());
        assertEquals(80, result.get("11").get("JAN"));
        assertEquals(75, result.get("11").get("FEB"));

        assertEquals(90, result.get("12").get("JAN"));
    }

    @Test
    void testGetMonthlyChart_EmptyData() {

        when(studentRepository.getMonthlyCountFiltered(null, null))
                .thenReturn(Collections.emptyList());

        assertThrows(MonthlyChartCustomException.class,
                () -> chartService.getMonthlyChart(null, null));
    }

    @Test
    void testGetMonthlyChart_NullList() {

        when(studentRepository.getMonthlyCountFiltered(null, null))
                .thenReturn(null);

        assertThrows(MonthlyChartCustomException.class,
                () -> chartService.getMonthlyChart(null, null));
    }

    @Test
    void testGetMonthlyChart_InvalidMonth() {

        List<Object[]> mockRows = Collections.singletonList(
                new Object[]{13, "11", 70} // invalid month
        );

        when(studentRepository.getMonthlyCountFiltered("11", "2024"))
                .thenReturn(mockRows);

        assertThrows(MonthlyChartCustomException.class,
                () -> chartService.getMonthlyChart("11", "2024"));
    }
}
