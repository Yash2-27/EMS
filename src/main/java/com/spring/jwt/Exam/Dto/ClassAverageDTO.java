package com.spring.jwt.Exam.Dto;

public class ClassAverageDTO {
    private String month;
    private Double averagePercentage;

    public ClassAverageDTO(String month, Double averagePercentage) {
        this.month = month;
        this.averagePercentage = averagePercentage;
    }

    public String getMonth() { return month; }
    public Double getAveragePercentage() { return averagePercentage; }
}
