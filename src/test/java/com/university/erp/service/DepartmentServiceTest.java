package com.university.erp.service;

import com.university.erp.entity.Department;
import com.university.erp.repository.DepartmentRepository;
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
class DepartmentServiceTest {

    @Mock // Creates a mock (fake) DepartmentRepository
    private DepartmentRepository departmentRepository;

    @InjectMocks // Injects the mock into DepartmentService
    private DepartmentService departmentService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Create fresh test data before each test
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");
    }

    @Test
    void testGetAllDepartments() {
        // Arrange: Define what the mock should return
        List<Department> departments = Arrays.asList(testDepartment);
        when(departmentRepository.findAll()).thenReturn(departments);

        // Act: Call the actual service method
        List<Department> result = departmentService.getAllDepartments();

        // Assert: Verify the result
        assertEquals(1, result.size());
        assertEquals("Computer Science", result.get(0).getName());

        // Verify the repository method was called exactly once
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentById_Found() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));

        // Act
        Optional<Department> result = departmentService.getDepartmentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Computer Science", result.get().getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDepartmentById_NotFound() {
        // Arrange
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Department> result = departmentService.getDepartmentById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateDepartment() {
        // Arrange
        when(departmentRepository.save(testDepartment)).thenReturn(testDepartment);

        // Act
        Department result = departmentService.createDepartment(testDepartment);

        // Assert
        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        verify(departmentRepository, times(1)).save(testDepartment);
    }

    @Test
    void testUpdateDepartment_Found() {
        // Arrange
        Department updateDetails = new Department();
        updateDetails.setName("Updated Department");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment));
        when(departmentRepository.save(any(Department.class))).thenReturn(testDepartment);

        // Act
        Department result = departmentService.updateDepartment(1L, updateDetails);

        // Assert
        assertNotNull(result);
        verify(departmentRepository, times(1)).findById(1L);
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    void testUpdateDepartment_NotFound() {
        // Arrange
        Department updateDetails = new Department();
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Should throw RuntimeException
        assertThrows(RuntimeException.class, () -> {
            departmentService.updateDepartment(999L, updateDetails);
        });
    }

    @Test
    void testDeleteDepartment() {
        // Act
        departmentService.deleteDepartment(1L);

        // Assert: Verify repository method was called
        verify(departmentRepository, times(1)).deleteById(1L);
    }
}