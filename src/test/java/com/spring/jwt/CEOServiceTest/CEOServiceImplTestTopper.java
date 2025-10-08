package com.spring.jwt.CEOServiceTest;

import com.spring.jwt.CEO.DashboredDTO;
import com.spring.jwt.CEO.CEOServiceImpl;
import com.spring.jwt.entity.Student;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.Exam.repository.ExamResultRepository;
import com.spring.jwt.repository.StudentRepository;
import com.spring.jwt.Exam.entity.ExamResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CEOServiceImplTestTopper {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ExamResultRepository examResultRepository;

    @InjectMocks
    private CEOServiceImpl service;

    private Student student1, student2, student3;
    private User user1, user2, user3;
    private List<ExamResult> examResults;

    @BeforeEach
    void setUp() {
        user1 = new User(); user1.setId(1); user1.setFirstName("Pratham"); user1.setLastName("Khaire"); user1.setEmail("pratham@example.com");
        user2 = new User(); user2.setId(2); user2.setFirstName("Amit"); user2.setLastName("Kamble"); user2.setEmail("amit@example.com");
        user3 = new User(); user3.setId(3); user3.setFirstName("Yash"); user3.setLastName("Kadam"); user3.setEmail("yash@example.com");

        student1 = new Student(); student1.setUserId(1); student1.setName("Pratham"); student1.setLastName("Khaire"); student1.setStudentClass("11"); student1.setBatch("2025");
        student2 = new Student(); student2.setUserId(2); student2.setName("Amit"); student2.setLastName("Kamble"); student2.setStudentClass("11"); student2.setBatch("2025");
        student3 = new Student(); student3.setUserId(3); student3.setName("Yash"); student3.setLastName("Kadam"); student3.setStudentClass("11"); student3.setBatch("2025");

        examResults = List.of(
                createExamResult(user1, 90), createExamResult(user1, 85), createExamResult(user1, 92), createExamResult(user1, 88),
                createExamResult(user2, 78), createExamResult(user2, 82), createExamResult(user2, 80), createExamResult(user2, 85),
                createExamResult(user3, 70), createExamResult(user3, 75), createExamResult(user3, 65), createExamResult(user3, 60)
        );
    }

    private ExamResult createExamResult(User user, double score) {
        ExamResult er = new ExamResult();
        er.setUser(user);
        er.setScore(score);
        er.setTotalMarks(100.0);
        return er;
    }

    @Test
    void testCalculateStudentPercentages_multipleResults() {
        List<Student> students = List.of(student1, student2, student3);
        Map<Integer, Double> percentages = service.calculateStudentPercentages(students, examResults);

        assertEquals(3, percentages.size());
        assertEquals(88.75, percentages.get(1), 0.01);
        assertEquals(81.25, percentages.get(2), 0.01);
        assertEquals(67.5, percentages.get(3), 0.01);
    }

    @Test
    void testGetBatchToppers_multipleResults() {
        when(studentRepository.findByStudentClassAndBatch("11", "2025")).thenReturn(List.of(student1, student2, student3));
        when(examResultRepository.findByUser_IdIn(List.of(1, 2, 3))).thenReturn(examResults);

        List<DashboredDTO> toppers = service.getBatchToppers("11", "2025");

        assertEquals(2, toppers.size());
        assertEquals("Pratham", toppers.get(0).getName());
        assertEquals(88.75, toppers.get(0).getPercentage(), 0.01);
        assertEquals("Amit", toppers.get(1).getName());
        assertEquals(81.25, toppers.get(1).getPercentage(), 0.01);
    }

    @Test
    void testGetBatchToppers_noStudentsFound() {
        when(studentRepository.findByStudentClassAndBatch("11", "2025")).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> service.getBatchToppers("11", "2025"));
    }

    @Test
    void testGetBatchToppers_invalidInput() {
        assertThrows(IllegalArgumentException.class, () -> service.getBatchToppers(null, "2025"));
        assertThrows(IllegalArgumentException.class, () -> service.getBatchToppers("11", ""));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    @NullSource
    void testGetBatchToppers_invalidStudentClass_throwsException(String invalidClass) {
        assertThrows(IllegalArgumentException.class, () -> service.getBatchToppers(invalidClass, "2025"));
    }


}
