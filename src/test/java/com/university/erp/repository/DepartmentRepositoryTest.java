package com.university.erp.repository;

import com.university.erp.entity.Department;
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
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        departmentRepository.deleteAll();

        testDepartment = new Department();
        testDepartment.setName("Computer Science");
    }

    @Test
    void testSaveDepartment() {
        Department saved = departmentRepository.save(testDepartment);

        assertNotNull(saved.getId());
        assertEquals("Computer Science", saved.getName());
    }

    @Test
    void testFindById_Found() {
        Department saved = departmentRepository.save(testDepartment);

        Optional<Department> found = departmentRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Computer Science", found.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Department> found = departmentRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        departmentRepository.save(testDepartment);

        Department dept2 = new Department();
        dept2.setName("Mathematics");
        departmentRepository.save(dept2);

        List<Department> departments = departmentRepository.findAll();

        assertEquals(2, departments.size());
    }

    @Test
    void testDeleteDepartment() {
        Department saved = departmentRepository.save(testDepartment);

        departmentRepository.deleteById(saved.getId());

        Optional<Department> found = departmentRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateDepartment() {
        Department saved = departmentRepository.save(testDepartment);

        saved.setName("Electrical Engineering");
        Department updated = departmentRepository.save(saved);

        assertEquals("Electrical Engineering", updated.getName());
    }
}
