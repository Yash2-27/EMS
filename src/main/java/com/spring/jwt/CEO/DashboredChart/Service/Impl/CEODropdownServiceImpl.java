package com.spring.jwt.CEO.DashboredChart.Service.Impl;

import com.spring.jwt.CEO.DashboredChart.Service.DropdownService;
import com.spring.jwt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CEODropdownServiceImpl implements DropdownService {
    private final StudentRepository studentRepository;

    @Override
    public List<String> getStudentClass() {
        return studentRepository.findDistinctStudentClasses();
    }

    @Override
    public List<String> getStudentBatch() {
        return studentRepository.findByStudentBatch();
    }

}
