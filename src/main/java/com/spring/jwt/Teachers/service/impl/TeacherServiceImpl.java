package com.spring.jwt.Teachers.service.impl;
import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import com.spring.jwt.Teachers.service.TeacherService;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private TeacherInfoDto mapToDTO(Teacher teacher) {
        User user = teacher.getUser(); // Assuming Teacher entity has a getUser() method
        return new TeacherInfoDto(
                teacher.getTeacherId(),
                teacher.getName(),
                teacher.getSub(),
                teacher.getDeg(),
                teacher.getStatus(),
                user != null ? user.getEmail() : null,
                user != null ? user.getMobileNumber() : null,
                user != null ? user.getId() : null
        );
    }

    @Override
    public List<TeacherInfoDto> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TeacherInfoDto> findById(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .map(this::mapToDTO);
    }


}
