package com.spring.jwt.CEO;

import java.util.List;

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

}
