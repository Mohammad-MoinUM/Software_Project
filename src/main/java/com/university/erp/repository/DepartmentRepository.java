package com.university.erp.repository;

import com.university.erp.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository  extends JpaRepository<Department, Long> {
}
