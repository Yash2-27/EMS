package com.spring.jwt.entity;

import jakarta.persistence.*;
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

    private String name;
    private String batch;
    private String studentName;
    private Integer studentId;
    private String relationshipWithStudent;
}
