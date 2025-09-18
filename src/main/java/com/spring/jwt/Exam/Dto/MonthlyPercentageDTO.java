package com.spring.jwt.Exam.Dto;

public class MonthlyPercentageDTO {
    private String month;
    private Double percentage;

    public MonthlyPercentageDTO(String month, Double percentage) {
        this.month = month;
        this.percentage = percentage;
    }

    // Getters & Setters
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}

