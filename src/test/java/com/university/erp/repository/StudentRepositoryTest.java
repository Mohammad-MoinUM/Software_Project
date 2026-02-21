package com.university.erp.repository;

import com.university.erp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Loads only JPA context with H2 in-memory database
@Transactional // Rolls back database changes after each test
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        studentRepository.deleteAll();

        // Create fresh test data
        testStudent = new Student();
        testStudent.setName("Test Student");
        testStudent.setRoll(1001);
        testStudent.setEmail("test@university.edu");
        testStudent.setPassword("encryptedPassword123");
        testStudent.setRole("STUDENT");
    }

    @Test
    void testSaveStudent() {
        // Act: Save student to database
        Student saved = studentRepository.save(testStudent);

        // Assert
        assertNotNull(saved.getId()); // ID should be auto-generated
        assertEquals("Test Student", saved.getName());
        assertEquals("test@university.edu", saved.getEmail());
    }

    @Test
    void testFindById_Found() {
        // Arrange: Save first
        Student saved = studentRepository.save(testStudent);

        // Act: Find by ID
        Optional<Student> found = studentRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Test Student", found.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        // Act: Try to find non-existent ID
        Optional<Student> found = studentRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmail_Found() {
        // Arrange: Save first
        studentRepository.save(testStudent);

        // Act: Find by email
        Optional<Student> found = studentRepository.findByEmail("test@university.edu");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("test@university.edu", found.get().getEmail());
        assertEquals("Test Student", found.get().getName());
    }

    @Test
    void testFindByEmail_NotFound() {
        // Act: Try to find non-existent email
        Optional<Student> found = studentRepository.findByEmail("notfound@university.edu");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        // Arrange: Save multiple students
        studentRepository.save(testStudent);

        Student student2 = new Student();
        student2.setName("Student Two");
        student2.setRoll(1002);
        student2.setEmail("student2@university.edu");
        student2.setPassword("password123");
        student2.setRole("STUDENT");
        studentRepository.save(student2);

        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        assertEquals(2, students.size());
    }

    @Test
    void testDeleteStudent() {
        // Arrange: Save first
        Student saved = studentRepository.save(testStudent);

        // Act: Delete
        studentRepository.deleteById(saved.getId());

        // Assert: Should be gone
        Optional<Student> found = studentRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateStudent() {
        // Arrange: Save first
        Student saved = studentRepository.save(testStudent);

        // Act: Update
        saved.setName("Updated Name");
        saved.setRoll(2002);
        Student updated = studentRepository.save(saved);

        // Assert
        assertEquals("Updated Name", updated.getName());
        assertEquals(2002, updated.getRoll());
    }
}