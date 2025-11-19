package com.spring.jwt.CEO.DashboredChart.Service.Impl;

import com.spring.jwt.CEO.DashboredChart.Service.ChartService;
import com.spring.jwt.exception.MonthlyChartCustomException;
import com.spring.jwt.exception.PieChartCustomException;
import com.spring.jwt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {
    private final StudentRepository studentRepository;

    @Override
    public Map<String, Long> getPieChart() {

        try {
            List<Object[]> rows = studentRepository.countByClass();

            if (rows == null) {
                throw new PieChartCustomException("Database returned null for pie chart data");
            }

            if (rows.isEmpty()) {
                throw new PieChartCustomException("No student records found to generate pie chart");
            }

            Map<String, Long> pie = new HashMap<>();
            for (Object[] row : rows) {

                if (row[0] == null || row[1] == null) {
                    throw new PieChartCustomException("Invalid data found in pie chart query result");
                }
                String cls = String.valueOf(row[0]);
                Long count = ((Number) row[1]).longValue();
                pie.put("Students Class " + cls, count);
            }
            return pie;
        } catch (PieChartCustomException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PieChartCustomException("Failed to fetch pie chart data: " + ex.getMessage());
        }
    }

    public Map<String, Map<String, Integer>> getMonthlyChart(String studentClass, String batch) {

        try {
            Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
            result.put("11", new LinkedHashMap<>());
            result.put("12", new LinkedHashMap<>());

            List<Object[]> rows = studentRepository.getMonthlyCountFiltered(studentClass, batch);

            if (rows == null) {
                throw new MonthlyChartCustomException("Database returned null for monthly chart data");
            }

            if (rows.isEmpty()) {
                throw new MonthlyChartCustomException("No attendance data available for selected filter");
            }

            for (Object[] row : rows) {

                if (row == null || row.length < 3) {
                    throw new MonthlyChartCustomException("Invalid data row returned from monthly chart query");
                }

                if (row[0] == null || row[1] == null || row[2] == null) {
                    throw new MonthlyChartCustomException("Null value found in monthly chart data");
                }

                Integer month = ((Number) row[0]).intValue();   // 1–12
                String cls = row[1].toString();                 // 11 / 12
                Integer pct = ((Number) row[2]).intValue();     // Percentage

                if (month < 1 || month > 12) {
                    throw new MonthlyChartCustomException("Invalid month number: " + month);
                }

                if (!result.containsKey(cls)) {
                    throw new MonthlyChartCustomException("Unexpected class found: " + cls);
                }

                String monthName = Month.of(month).name().substring(0, 3);
                result.get(cls).put(monthName, pct);
            }

            return result;

        } catch (MonthlyChartCustomException ex) {
            throw ex;  // rethrow custom exception
        } catch (Exception ex) {
            throw new MonthlyChartCustomException("Failed to fetch monthly chart data: " + ex.getMessage());
        }
    }

}

