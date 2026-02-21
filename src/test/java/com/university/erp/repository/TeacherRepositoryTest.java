package com.university.erp.repository;

import com.university.erp.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();

        testTeacher = new Teacher();
        testTeacher.setName("Dr. Smith");
        testTeacher.setEmployeeId(5001);
        testTeacher.setEmail("smith@university.edu");
        testTeacher.setPassword("encryptedPassword123");
        testTeacher.setRole("TEACHER");
    }

    @Test
    void testSaveTeacher() {
        Teacher saved = teacherRepository.save(testTeacher);

        assertNotNull(saved.getId());
        assertEquals("Dr. Smith", saved.getName());
        assertEquals(5001, saved.getEmployeeId());
        assertEquals("smith@university.edu", saved.getEmail());
    }

    @Test
    void testFindById_Found() {
        Teacher saved = teacherRepository.save(testTeacher);

        Optional<Teacher> found = teacherRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Dr. Smith", found.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Teacher> found = teacherRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmail_Found() {
        teacherRepository.save(testTeacher);

        Optional<Teacher> found = teacherRepository.findByEmail("smith@university.edu");

        assertTrue(found.isPresent());
        assertEquals("smith@university.edu", found.get().getEmail());
        assertEquals("Dr. Smith", found.get().getName());
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<Teacher> found = teacherRepository.findByEmail("notfound@university.edu");

        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByEmail_True() {
        teacherRepository.save(testTeacher);

        assertTrue(teacherRepository.existsByEmail("smith@university.edu"));
    }

    @Test
    void testExistsByEmail_False() {
        assertFalse(teacherRepository.existsByEmail("notfound@university.edu"));
    }

    @Test
    void testFindAll() {
        teacherRepository.save(testTeacher);

        Teacher teacher2 = new Teacher();
        teacher2.setName("Dr. Johnson");
        teacher2.setEmployeeId(5002);
        teacher2.setEmail("johnson@university.edu");
        teacher2.setPassword("password456");
        teacher2.setRole("TEACHER");
        teacherRepository.save(teacher2);

        List<Teacher> teachers = teacherRepository.findAll();

        assertEquals(2, teachers.size());
    }

    @Test
    void testDeleteTeacher() {
        Teacher saved = teacherRepository.save(testTeacher);

        teacherRepository.deleteById(saved.getId());

        Optional<Teacher> found = teacherRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateTeacher() {
        Teacher saved = teacherRepository.save(testTeacher);

        saved.setName("Dr. Smith Jr.");
        saved.setEmployeeId(5010);
        Teacher updated = teacherRepository.save(saved);

        assertEquals("Dr. Smith Jr.", updated.getName());
        assertEquals(5010, updated.getEmployeeId());
    }
}
