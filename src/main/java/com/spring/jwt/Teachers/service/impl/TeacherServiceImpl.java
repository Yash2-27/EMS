package com.spring.jwt.Teachers.service.impl;
import com.spring.jwt.Teachers.dto.TeacherInfoDto;
import com.spring.jwt.Teachers.service.TeacherService;
import com.spring.jwt.entity.Teacher;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.TeacherRepository;
import com.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    private TeacherInfoDto mapToDTO(Teacher teacher) {
        // Fetch user manually using userId
        User user = userRepository.findById(Long.valueOf(teacher.getUserId())).orElse(null);

        TeacherInfoDto dto = new TeacherInfoDto();
        dto.setTeacherId(teacher.getTeacherId());
        dto.setName(teacher.getName());
        dto.setSub(teacher.getSub());
        dto.setDeg(teacher.getDeg());
        dto.setStatus(teacher.getStatus());
        dto.setUserId(teacher.getUserId());
        dto.setEmail(user != null ? user.getEmail() : null);
        dto.setMobileNumber(user != null ? user.getMobileNumber() : null);
        return dto;
    }

    @Override
    public List<TeacherInfoDto> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TeacherInfoDto> findById(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .map(this::mapToDTO);
    }
}