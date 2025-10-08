package com.spring.jwt.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teacherId;

    private String name;
    private String sub;
    private String deg;
    private String status;
    private Integer userId;

    /**

    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;


    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Classes> classes;

}
