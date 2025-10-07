package com.spring.jwt.Teachers.service.impl;
import com.spring.jwt.Teachers.dto.TeacherQuestionFlatDto;
import com.spring.jwt.Teachers.service.DropdownService;
import com.spring.jwt.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DropdownServiceImpl implements DropdownService {

     private final TeacherRepository teacherRepository;

    @Override
    public List<String> getAllClasses() {
        return teacherRepository.findAllClass();
    }

    @Override
    public List<Object> getTeachersByClasses(String studentClass) {
        return teacherRepository.findTeachersByClasses(studentClass);
    }

    @Override
    public List<String> getSubjects(String studentClass, Integer teacherId) {
        return teacherRepository.findTeachersBySubject(studentClass,teacherId);
    }

    @Override
    public List<String> getTitles(String studentClass, Integer teacherId, String subject) {
        return teacherRepository.findSubjectByTitles(studentClass,teacherId,subject);
    }

    @Override
    public List<TeacherQuestionFlatDto> getQuestionPaper(String studentClass,Integer teacherId,String subject,String title) {
        return teacherRepository.findByQuestionPaper(studentClass, teacherId, subject, title);
    }

}
