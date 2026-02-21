package com.university.erp.service;

import com.university.erp.entity.Course;
import com.university.erp.repository.CourseRepository;
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
class CourseServiceTest {

    @Mock // Creates a mock (fake) CourseRepository
    private CourseRepository courseRepository;

    @InjectMocks // Injects the mock into CourseService
    private CourseService courseService;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        // Create fresh test data before each test
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Introduction to Java");
        testCourse.setCode("CSE101");
        testCourse.setCredit(3.0f);
    }

    @Test
    void testGetAllCourses() {
        // Arrange: Define what the mock should return
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findAll()).thenReturn(courses);

        // Act: Call the actual service method
        List<Course> result = courseService.getAllCourses();

        // Assert: Verify the result
        assertEquals(1, result.size());
        assertEquals("Introduction to Java", result.get(0).getName());
        assertEquals("CSE101", result.get(0).getCode());

        // Verify the repository method was called exactly once
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCourseById_Found() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Optional<Course> result = courseService.getCourseById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Introduction to Java", result.get().getName());
        assertEquals("CSE101", result.get().getCode());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourseById_NotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Course> result = courseService.getCourseById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateCourse() {
        // Arrange
        when(courseRepository.save(testCourse)).thenReturn(testCourse);

        // Act
        Course result = courseService.createCourse(testCourse);

        // Assert
        assertNotNull(result);
        assertEquals("Introduction to Java", result.getName());
        assertEquals("CSE101", result.getCode());
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void testUpdateCourse_Found() {
        // Arrange
        Course updateDetails = new Course();
        updateDetails.setName("Advanced Java");
        updateDetails.setCode("CSE201");
        updateDetails.setCredit(4.0f);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course result = courseService.updateCourse(1L, updateDetails);

        // Assert
        assertNotNull(result);
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testUpdateCourse_NotFound() {
        // Arrange
        Course updateDetails = new Course();
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Should throw RuntimeException
        assertThrows(RuntimeException.class, () -> {
            courseService.updateCourse(999L, updateDetails);
        });
    }

    @Test
    void testDeleteCourse() {
        // Act
        courseService.deleteCourse(1L);

        // Assert: Verify repository method was called
        verify(courseRepository, times(1)).deleteById(1L);
    }
} 