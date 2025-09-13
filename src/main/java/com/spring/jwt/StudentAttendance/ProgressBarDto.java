package com.spring.jwt.StudentAttendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressBarDto {
    private Integer studentMonthlyScore;   // Individual student's monthly score
    private Integer classAverageMonthlyScore; // Class average monthly score
}
