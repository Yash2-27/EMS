package com.spring.jwt.CEO.DashboredChart.Service;

import java.util.List;
import java.util.Map;

public interface ChartService {
    Map<String, List<Integer>> getBarChart();

    Map<String, Long> getPieChart();
}
