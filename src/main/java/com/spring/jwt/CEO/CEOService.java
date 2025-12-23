package com.spring.jwt.CEO;

import java.util.List;
import java.util.Map;

public interface CEOService {
    /**
     * Get toppers of a batch based on class and batch
     * Percentage between 75 and 100
     *
     * @param studentClass Student class
     * @param batch        Batch year
     * @return List of toppers
     */
    List<DashboredDTO> getBatchToppers(String studentClass, String batch);

        List<DashboredDTO> getAverageStudents(String studentClass, String batch);

        List<DashboredDTO> getBelowAverageStudents(String studentClass, String batch);

    List<String> getStudentClass();
    List<String> getStudentBatch();
    Map<String, Long> getPieChart();
    Map<String, Map<String, Integer>> getMonthlyChart(String studentClass, String exam);

}
