package com.spring.jwt.CEO.DashboredChart.Service.Impl;

import com.spring.jwt.CEO.DashboredChart.Service.ChartService;
import com.spring.jwt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final StudentRepository studentRepository;

    @Override
    public Map<String, Long> getPieChart() {
        Map<String, Long> pie = new HashMap<>();

        for (Object[] row : studentRepository.countByClass()) {

            // FIXED CASTING
            String cls = String.valueOf(row[0]);  // Always safe
            Long count = ((Number) row[1]).longValue(); // SAFE CAST

            pie.put("Students Class " + cls, count);
        }


        return pie;
    }

    @Override
    public Map<String, List<Integer>> getBarChart() {

        Map<String, List<Integer>> monthly = new HashMap<>();
        monthly.put("11th", new ArrayList<>(Collections.nCopies(12, 0)));
        monthly.put("12th", new ArrayList<>(Collections.nCopies(12, 0)));

        for (Object[] row : studentRepository.getMonthlyCount()) {
            Integer month = ((Number) row[0]).intValue() - 1;
            String cls = (String) row[1];
            Number num = (Number) row[2];
            Integer count = num.intValue();                    // Convert safely

            if (cls.equals("11")) {
                monthly.get("11th").set(month, count.intValue());
            }
            if (cls.equals("12")) {
                monthly.get("12th").set(month, count.intValue());
            }
        }
        return monthly;
    }
}

