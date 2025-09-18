package com.spring.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfoDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String relationshipWithStudent;
    private String totalFees;
    private String amount;
    private String remainingFees;
}
