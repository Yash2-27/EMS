//parent service imp
package com.spring.jwt.Exam.serviceImpl;

import com.spring.jwt.Exam.service.ParentStudentService;
import com.spring.jwt.dto.StudentDTO;
import com.spring.jwt.entity.Student;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParentStudentServiceImpl implements ParentStudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Override
    public List<StudentDTO> getStudentsByParentId(Integer parentId) {
        if (parentId == null) {
            throw new IllegalArgumentException("Parent ID cannot be null.");
        }

        List<Student> students;
        try {
            students = studentRepository.findByParent_ParentsId(parentId);
        } catch (Exception e) {

            throw new RuntimeException("Error retrieving students from the database.", e);
        }

        if (students == null || students.isEmpty()) {
            throw new ResourceNotFoundException("No students found for parent ID: " + parentId);
        }

        try {
            return students.stream()
                    .map(StudentDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {

            throw new RuntimeException("Failed to map students to DTOs.", e);
        }
    }

}
