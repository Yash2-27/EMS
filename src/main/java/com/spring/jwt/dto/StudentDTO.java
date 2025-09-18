package com.spring.jwt.dto;

import com.spring.jwt.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//student dto


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Integer studentId;
    private String name;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String batch;
    private String studentcol1;
    private String studentClass;
    private Integer userId;
    private Integer parentsId;

    public static StudentDTO fromEntity(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO dto = new StudentDTO();
        dto.setStudentId(student.getStudentId());
        dto.setName(student.getName());
        dto.setLastName(student.getLastName());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setAddress(student.getAddress());
        dto.setBatch(student.getBatch());
        dto.setStudentcol1(student.getStudentcol1());
        dto.setStudentClass(student.getStudentClass());
        dto.setUserId(student.getUserId());


        if (student.getParent() != null) {
            dto.setParentsId(student.getParent().getParentsId());
        } else {
            dto.setParentsId(null);
        }

        return dto;
    }
}
