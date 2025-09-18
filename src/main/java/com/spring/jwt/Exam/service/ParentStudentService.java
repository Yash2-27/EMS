package com.spring.jwt.Exam.service;

import com.spring.jwt.dto.StudentDTO;
import java.util.List;

public interface ParentStudentService {

    /**
     * Retrieves a list of StudentDTOs associated with a given parent ID.
     *
     * @param parentId The ID of the parent.
     * @return A list of StudentDTOs.
     */
    List<StudentDTO> getStudentsByParentId(Integer parentId);
}