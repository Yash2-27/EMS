package com.spring.jwt.QuestionBankTest;

import com.spring.jwt.Question.*;
import com.spring.jwt.entity.Question;
import com.spring.jwt.exception.ResourceNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionBankServiceImplTest {

    @Mock
    private QuestionBankRepository questionBankRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionBankServiceImpl questionBankService;

    private Question sampleQuestion;

    @BeforeEach
    void setup() {
        sampleQuestion = new Question();
        sampleQuestion.setTopic("Inheritance");
        sampleQuestion.setSubject("Java");
        sampleQuestion.setStudentClass("12");
    }

    @Test
    void testGetTeachersByStudentClass_Success() {
        QuestionBankDTO dto1 = new QuestionBankDTO();
        dto1.setTeacherName("Mr. Smith");

        when(questionBankRepository.findTeachersByStudentClass("11"))
                .thenReturn(List.of(dto1));

        List<QuestionBankDTO> result = questionBankService.getTeachersByStudentClass("11");

        assertEquals(1, result.size());
        assertEquals("Mr. Smith", result.get(0).getTeacherName());

        verify(questionBankRepository, times(1)).findTeachersByStudentClass("11");
    }

    @Test
    void testGetTeachersByStudentClass_ThrowsResourceNotFound() {
        when(questionBankRepository.findTeachersByStudentClass("11"))
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> questionBankService.getTeachersByStudentClass("11"));

        assertTrue(exception.getMessage().contains("No teachers found for student class"));
    }

    // -------------------- getTopicsBySubjectAndStudentClass --------------------

    @Test
    void testGetTopicsBySubjectAndStudentClass_Success() {
        Question q1 = new Question();
        q1.setTopic("Inheritance");
        Question q2 = new Question();
        q2.setTopic("Polymorphism");

        when(questionRepository.findBySubjectAndStudentClass("Java", "12"))
                .thenReturn(List.of(q1, q2));

        List<QuestionBankSubjectDropdown> result =
                questionBankService.getTopicsBySubjectAndStudentClass("Java", "12");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getTopic().equals("Inheritance")));
        assertTrue(result.stream().anyMatch(d -> d.getTopic().equals("Polymorphism")));

        verify(questionRepository, times(1))
                .findBySubjectAndStudentClass("Java", "12");
    }

    @Test
    void testGetTopicsBySubjectAndStudentClass_ThrowsResourceNotFound() {
        when(questionRepository.findBySubjectAndStudentClass("Java", "12"))
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> questionBankService.getTopicsBySubjectAndStudentClass("Java", "12"));

        assertTrue(exception.getMessage().contains("No topics found"));
    }

    // -------------------- getFilteredQuestions --------------------

    @Test
    void testGetFilteredQuestions() {
        QuestionBankQuestionsDTO dto = new QuestionBankQuestionsDTO();
        dto.setQuestionText("What is Inheritance?");

        when(questionRepository.findFilteredQuestions("12", "Mr. Smith", "Java", "OOP"))
                .thenReturn(List.of(dto));

        List<QuestionBankQuestionsDTO> result = questionBankService.getFilteredQuestions(
                "12", "Mr. Smith", "Java", "OOP"
        );

        assertEquals(1, result.size());
        assertEquals("What is Inheritance?", result.get(0).getQuestionText());
    }


    @Test
    void testGetFilteredQuestions_ThrowsResourceNotFound() {
        when(questionRepository.findFilteredQuestions("12", "Mr. Smith", "Java", "Inheritance"))
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> questionBankService.getFilteredQuestions("12", "Mr. Smith", "Java", "Inheritance"));

        assertTrue(exception.getMessage().contains("No Qustion Bank Questions found"));
    }

}
