package com.hms.department.repository;

import com.hms.department.model.DepartmentDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentDoctorRepository extends JpaRepository<DepartmentDoctor, Long> {

    // Find all doctors in a department
    List<DepartmentDoctor> findByDepartmentId(Long departmentId);

    // Find specific doctor in a department
    Optional<DepartmentDoctor> findByDepartmentIdAndDoctorId(Long departmentId, Long doctorId);

    // Find head doctor of a department
    Optional<DepartmentDoctor> findByDepartmentIdAndIsHeadTrue(Long departmentId);

    // Check if doctor already exists in department
    boolean existsByDepartmentIdAndDoctorId(Long departmentId, Long doctorId);
}
