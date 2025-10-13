package com.spring.jwt.PersonalInfoTest;

import com.spring.jwt.dto.PersonalInfoDTO;
import com.spring.jwt.entity.Parents;
import com.spring.jwt.entity.Student;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.DuplicateEmailException;
import com.spring.jwt.exception.InvalidPersonalInfoException;
import com.spring.jwt.exception.InvalidPhoneNumberException;
import com.spring.jwt.exception.PersonalInfoResourceNotFoundException;
import com.spring.jwt.repository.ParentsRepository;
import com.spring.jwt.repository.StudentRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentsRepository parentsRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Student student;
    private Parents parent;
    private PersonalInfoDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);


        student = new Student();
        student.setStudentId(100);
        student.setUserId(user.getId());

        parent = new Parents();
        parent.setStudentId(100);
        parent.setFirstName("Jane");
        parent.setLastName("Doe");
        parent.setEmail("jane@example.com");
        parent.setMobileNumber(9999999999L);
        parent.setRelationshipWithStudent("Mother");

        dto = new PersonalInfoDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Smith");
        dto.setEmail("alice@example.com");
        dto.setPhoneNumber("9876543210");
        dto.setRelationshipWithStudent("Mother");
    }

    @Test
    void testGetPersonalInfo_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(1)).thenReturn(student);
        when(parentsRepository.findByStudentId(100)).thenReturn(parent);

        PersonalInfoDTO result = userService.getPersonalInfo(1L);

        assertEquals("Jane", result.getFirstName());
        assertEquals("Mother", result.getRelationshipWithStudent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPersonalInfo_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PersonalInfoResourceNotFoundException.class, () ->
                userService.getPersonalInfo(99L)
        );
    }

    @Test
    void testUpdatePersonalInfo_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(1)).thenReturn(null);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        PersonalInfoDTO result = userService.updatePersonalInfo(1L, dto);

        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePersonalInfo_InvalidFirstName() {
        dto.setFirstName("1234");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(1)).thenReturn(null);

        assertThrows(InvalidPersonalInfoException.class, () ->
                userService.updatePersonalInfo(1L, dto)
        );
    }

    @Test
    void testUpdatePersonalInfo_DuplicateEmail() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(1)).thenReturn(null);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () ->
                userService.updatePersonalInfo(1L, dto)
        );
    }

    @Test
    void testUpdatePersonalInfo_InvalidPhoneNumber() {
        dto.setPhoneNumber("12AB");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(1)).thenReturn(null);

        assertThrows(InvalidPhoneNumberException.class, () ->
                userService.updatePersonalInfo(1L, dto)
        );
    }
}
