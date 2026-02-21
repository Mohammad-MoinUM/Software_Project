package com.university.erp.service;

import com.university.erp.entity.Student;
import com.university.erp.repository.StudentRepository;
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
class StudentServiceTest {

    @Mock // Creates a mock (fake) StudentRepository
    private StudentRepository studentRepository;

    @InjectMocks // Injects the mock into StudentService
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        // Create fresh test data before each test
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Test Student");
        testStudent.setRoll(1001);
        testStudent.setEmail("test@university.edu");
        testStudent.setPassword("encryptedPassword123");
        testStudent.setRole("STUDENT");
    }

    @Test
    void testGetAllStudents() {
        // Arrange: Define what the mock should return
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        // Act: Call the actual service method
        List<Student> result = studentService.getAllStudents();

        // Assert: Verify the result
        assertEquals(1, result.size());
        assertEquals("Test Student", result.get(0).getName());
        assertEquals("test@university.edu", result.get(0).getEmail());

        // Verify the repository method was called exactly once
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_Found() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> result = studentService.getStudentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Student", result.get().getName());
        assertEquals(1001, result.get().getRoll());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.getStudentById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateStudent() {
        // Arrange
        when(studentRepository.save(testStudent)).thenReturn(testStudent);

        // Act
        Student result = studentService.createStudent(testStudent);

        // Assert
        assertNotNull(result);
        assertEquals("Test Student", result.getName());
        assertEquals("test@university.edu", result.getEmail());
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    void testUpdateStudent_Found() {
        // Arrange
        Student updateDetails = new Student();
        updateDetails.setName("Updated Name");
        updateDetails.setRoll(2002);
        updateDetails.setEmail("updated@university.edu");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student result = studentService.updateStudent(1L, updateDetails);

        // Assert
        assertNotNull(result);
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound() {
        // Arrange
        Student updateDetails = new Student();
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Should throw RuntimeException
        assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(999L, updateDetails);
        });
    }

    @Test
    void testDeleteStudent() {
        // Act
        studentService.deleteStudent(1L);

        // Assert: Verify repository method was called
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByEmail_Found() {
        // Arrange
        when(studentRepository.findByEmail("test@university.edu")).thenReturn(Optional.of(testStudent));

        // Act
        Student result = studentService.findByEmail("test@university.edu");

        // Assert
        assertNotNull(result);
        assertEquals("Test Student", result.getName());
        assertEquals("test@university.edu", result.getEmail());
        verify(studentRepository, times(1)).findByEmail("test@university.edu");
    }

    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        when(studentRepository.findByEmail("notfound@university.edu")).thenReturn(Optional.empty());

        // Act & Assert: Should throw RuntimeException
        assertThrows(RuntimeException.class, () -> {
            studentService.findByEmail("notfound@university.edu");
        });
    }
}