package com.spring.jwt.CEO.DashboredChart.Service;

import java.util.Map;

public interface ChartService {
    Map<String, Long> getPieChart();

    Map<String, Map<String, Integer>> getMonthlyChart(String studentClass, String batch);
}
