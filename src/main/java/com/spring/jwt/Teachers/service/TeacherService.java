package com.spring.jwt.Teachers.service;


import com.spring.jwt.dto.TeacherDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface TeacherService {

    List<TeacherDTO> getAllTeachers();
    TeacherDTO getTeacherById(Integer teacherId);

}
