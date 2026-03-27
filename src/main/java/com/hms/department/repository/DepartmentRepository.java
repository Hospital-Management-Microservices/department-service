package com.hms.department.repository;

import com.hms.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Find departments by name (for search)
    List<Department> findByNameContainingIgnoreCase(String name);

    // Find all active departments
    List<Department> findByIsActiveTrue();
}
