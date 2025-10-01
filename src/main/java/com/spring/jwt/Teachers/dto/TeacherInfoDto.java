package com.spring.jwt.Teachers.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherInfoDto {
    private Integer teacherId;
    private String name;
    private String sub;
    private String deg;
    private String status;
    private String email;
    private Long mobileNumber;
    private Integer userId;

}
