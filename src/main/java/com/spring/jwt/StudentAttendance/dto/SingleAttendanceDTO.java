package com.spring.jwt.StudentAttendance.dto;

import lombok.Data;

@Data
public class SingleAttendanceDTO {
    private Integer userId;
    private Boolean attendance;
}
