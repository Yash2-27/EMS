package com.spring.jwt.SeeExamResult;

import com.spring.jwt.ExamResult.SeeExamResultDTO;
import com.spring.jwt.ExamResult.SeeExamResultRepository;
import com.spring.jwt.ExamResult.SeeExamResultServiceImpl;
import com.spring.jwt.exception.ExamResultNotFoundException;
import com.spring.jwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeeExamResultServiceImplTest {

    @Mock
    private SeeExamResultRepository examResultRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SeeExamResultServiceImpl seeExamResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStudentExamResult_Success() {
        Long userId = 1L;
        String subject = "Math";
        String topic = "Algebra";

        // Mock user existence
        when(userRepository.existsById(userId)).thenReturn(true);

        // Mock repository result
        Object[] resultRow = new Object[]{
                "Math", "Algebra", "2025-05-12", 10, 100, 8, 2, 85, 50, 5
        };
        List<Object[]> resultList = List.<Object[]>of(resultRow);

        when(examResultRepository.findStudentExamResult(userId, subject, topic))
                .thenReturn(resultList);

        // Call service
        SeeExamResultDTO dto = seeExamResultService.getStudentExamResult(userId, subject, topic);

        // Verify and assert
        assertNotNull(dto);
        assertEquals("Math", dto.getSubject());
        assertEquals("Algebra", dto.getTopic());
        assertEquals("2025-05-12", dto.getExamDate());
        assertEquals(10, dto.getNoOfQuestions());
        assertEquals(8, dto.getAnswered());
        assertEquals(2, dto.getNotAnswered());
        assertEquals(5, dto.getOverallRank());
        assertEquals(50, dto.getTotalStudents());
        assertEquals(85, dto.getStudentMarks());
        assertEquals(100, dto.getTotalMarks());

        verify(examResultRepository, times(1))
                .findStudentExamResult(userId, subject, topic);
    }

    @Test
    void testGetStudentExamResult_UserNotFound() {
        Long userId = 99L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ExamResultNotFoundException.class, () ->
                seeExamResultService.getStudentExamResult(userId, "Math", "Algebra")
        );
    }

    @Test
    void testGetStudentExamResult_NoResultsForSubject() {
        Long userId = 1L;
        String subject = "Physics";
        String topic = "Optics";

        when(userRepository.existsById(userId)).thenReturn(true);
        when(examResultRepository.findStudentExamResult(userId, subject, topic))
                .thenReturn(List.of()); // empty list
        when(examResultRepository.countBySubject(subject)).thenReturn(0);

        assertThrows(ExamResultNotFoundException.class, () ->
                seeExamResultService.getStudentExamResult(userId, subject, topic)
        );
    }
}
