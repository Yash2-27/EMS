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
        // 1. Validate input
        if (parentId == null) {
            throw new IllegalArgumentException("Parent ID cannot be null.");
        }

        List<Student> students;

        try {
            // 2. Retrieve students from the database
            students = studentRepository.findByParent_ParentsId(parentId);
        } catch (RuntimeException e) {
            // 3. More specific than generic Exception
            throw new RuntimeException("Database error while retrieving students for parent ID: " + parentId, e);
        }

        // 4. Handle empty result
        if (students == null || students.isEmpty()) {
            throw new ResourceNotFoundException("No students found for parent ID: " + parentId);
        }

        try {
            // 5. Map entities to DTOs
            return students.stream()
                    .map(StudentDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (IllegalStateException | NullPointerException e) {
            // 6. More specific exception block for mapping issues
            throw new RuntimeException("Error while mapping Student entities to DTOs.", e);
        } catch (Exception e) {
            // 7. Fallback for unexpected mapping issues
            throw new RuntimeException("Unexpected error during DTO conversion.", e);
        }
    }


}
