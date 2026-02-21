package com.university.erp.repository;

import com.university.erp.entity.Course;
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
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();

        testCourse = new Course();
        testCourse.setName("Introduction to Java");
        testCourse.setCode("CSE101");
        testCourse.setCredit(3.0f);
    }

    @Test
    void testSaveCourse() {
        Course saved = courseRepository.save(testCourse);

        assertNotNull(saved.getId());
        assertEquals("Introduction to Java", saved.getName());
        assertEquals("CSE101", saved.getCode());
        assertEquals(3.0f, saved.getCredit());
    }

    @Test
    void testFindById_Found() {
        Course saved = courseRepository.save(testCourse);

        Optional<Course> found = courseRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Introduction to Java", found.get().getName());
        assertEquals("CSE101", found.get().getCode());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Course> found = courseRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        courseRepository.save(testCourse);

        Course course2 = new Course();
        course2.setName("Data Structures");
        course2.setCode("CSE201");
        course2.setCredit(4.0f);
        courseRepository.save(course2);

        List<Course> courses = courseRepository.findAll();

        assertEquals(2, courses.size());
    }

    @Test
    void testDeleteCourse() {
        Course saved = courseRepository.save(testCourse);

        courseRepository.deleteById(saved.getId());

        Optional<Course> found = courseRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateCourse() {
        Course saved = courseRepository.save(testCourse);

        saved.setName("Advanced Java");
        saved.setCode("CSE301");
        saved.setCredit(4.0f);
        Course updated = courseRepository.save(saved);

        assertEquals("Advanced Java", updated.getName());
        assertEquals("CSE301", updated.getCode());
        assertEquals(4.0f, updated.getCredit());
    }
}
