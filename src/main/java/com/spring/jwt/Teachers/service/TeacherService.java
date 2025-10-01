package com.spring.jwt.Teachers.service;


import com.spring.jwt.Teachers.dto.PapersAndTeacherInfoDto;
import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface TeacherService {

    List<TeacherInfoDto> getAllTeachers();
    TeacherInfoDto getTeacherById(Integer teacherId);

    List<PapersAndTeacherInfoDto> getPapersByTeacherId(Integer teacherId);
}
