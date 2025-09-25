package com.spring.jwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parents")
public class Parents {

    @Id
    @Column(name = "parents_id") // 👈 required to fix mapping issue
    private Integer parentsId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Column(name = "email", nullable = false, length = 250, unique = true)
    private String email;

    @Column(name = "mobile_number")
    private Long mobileNumber;

    @Column(name = "relationship_with_student")
    private String relationshipWithStudent;

    private String name;
    private String batch;
    private String studentName;
    private Integer studentId;
}
