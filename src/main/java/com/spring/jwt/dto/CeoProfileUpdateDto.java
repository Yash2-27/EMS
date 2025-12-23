package com.spring.jwt.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CeoProfileUpdateDto {

    private String fullName;

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email format"
    )
    private String userName;

    @Pattern(
            regexp = "^[1   -9]\\d{9}$",
            message = "Mobile number must be a valid 10 digit number"
    )
    private String mobileNumber;

}
