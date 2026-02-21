package com.university.erp.service;

import com.university.erp.entity.Teacher;
import com.university.erp.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
class TeacherServiceTest {

    @Mock // Creates a mock (fake) TeacherRepository
    private TeacherRepository teacherRepository;

    @InjectMocks // Injects the mock into TeacherService
    private TeacherService teacherService;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        // Create fresh test data before each test
        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Test Teacher");
        testTeacher.setEmployeeId(5001);
        testTeacher.setEmail("test@university.edu");
        testTeacher.setPassword("encryptedPassword123");
        testTeacher.setRole("TEACHER");
    }

    @Test
    void testGetAllTeachers() {
        // Arrange: Define what the mock should return
        List<Teacher> teachers = Arrays.asList(testTeacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        // Act: Call the actual service method
        List<Teacher> result = teacherService.getAllTeachers();

        // Assert: Verify the result
        assertEquals(1, result.size());
        assertEquals("Test Teacher", result.get(0).getName());
        assertEquals("test@university.edu", result.get(0).getEmail());

        // Verify the repository method was called exactly once
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testGetTeacherById_Found() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

        // Act
        Optional<Teacher> result = teacherService.getTeacherById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Teacher", result.get().getName());
        assertEquals(5001, result.get().getEmployeeId());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTeacherById_NotFound() {
        // Arrange
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Teacher> result = teacherService.getTeacherById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(teacherRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateTeacher() {
        // Arrange
        when(teacherRepository.save(testTeacher)).thenReturn(testTeacher);

        // Act
        Teacher result = teacherService.createTeacher(testTeacher);

        // Assert
        assertNotNull(result);
        assertEquals("Test Teacher", result.getName());
        assertEquals("test@university.edu", result.getEmail());
        verify(teacherRepository, times(1)).save(testTeacher);
    }

    @Test
    void testUpdateTeacher_Found() {
        // Arrange
        Teacher updateDetails = new Teacher();
        updateDetails.setName("Updated Name");
        updateDetails.setEmployeeId(6001);
        updateDetails.setEmail("updated@university.edu");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // Act
        Teacher result = teacherService.updateTeacher(1L, updateDetails);

        // Assert
        assertNotNull(result);
        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void testUpdateTeacher_NotFound() {
        // Arrange
        Teacher updateDetails = new Teacher();
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Should throw RuntimeException
        assertThrows(RuntimeException.class, () -> {
            teacherService.updateTeacher(999L, updateDetails);
        });
    }

    @Test
    void testDeleteTeacher() {
        // Act
        teacherService.deleteTeacher(1L);

        // Assert: Verify repository method was called
        verify(teacherRepository, times(1)).deleteById(1L);
    }
}