package com.spring.jwt.Teachers.service.impl;

import com.spring.jwt.Teachers.service.TeacherService;
import com.spring.jwt.dto.TeacherDTO;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.TeacherRepository;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    private TeacherDTO mapToDTO(Teacher teacher, User user) {
        return new TeacherDTO(
                teacher.getTeacherId(),
                teacher.getName(),
                teacher.getSub(),
                teacher.getDeg(),
                teacher.getStatus(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getId()
        );
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(t -> {
                    User user = userRepository.findById(Long.valueOf(t.getUserId()))
                            .orElse(null);
                    assert user != null;
                    return mapToDTO(t, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDTO getTeacherById(Integer teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        User user = userRepository.findById(teacher.getUserId().longValue())
                .orElseThrow(() -> new RuntimeException("User not found for teacher id: " + teacherId));

        return mapToDTO(teacher, user);
    }

}
