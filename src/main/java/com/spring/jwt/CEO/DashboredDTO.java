package com.spring.jwt.CEO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboredDTO {
    private Integer userId;
    private String name;
    private String lastName;
    private String studentClass;
    private String batch;
    private Double percentage;

}
