package com.hms.department.controller;

import com.hms.department.dto.*;
import com.hms.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Tag(name = "Department Service", description = "Hospital Department Management API")
public class DepartmentController {

    private final DepartmentService departmentService;

    // ==================== DEPARTMENT CRUD ====================

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(
            @RequestBody DepartmentRequestDTO request) {
        DepartmentResponseDTO response = departmentService.createDepartment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentRequestDTO request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully!");
    }

    @GetMapping("/search")
    @Operation(summary = "Search departments by name")
    public ResponseEntity<List<DepartmentResponseDTO>> searchDepartments(
            @RequestParam String name) {
        return ResponseEntity.ok(departmentService.searchDepartments(name));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active departments")
    public ResponseEntity<List<DepartmentResponseDTO>> getActiveDepartments() {
        return ResponseEntity.ok(departmentService.getActiveDepartments());
    }

    // ==================== DOCTOR MANAGEMENT ====================

    @PostMapping("/{id}/doctors")
    @Operation(summary = "Add doctor to department (fetches from Doctor Service)")
    public ResponseEntity<DepartmentDoctorDTO> addDoctorToDepartment(
            @PathVariable Long id,
            @RequestBody AddDoctorRequestDTO request) {
        return new ResponseEntity<>(
                departmentService.addDoctorToDepartment(id, request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}/doctors")
    @Operation(summary = "Get all doctors in a department")
    public ResponseEntity<List<DepartmentDoctorDTO>> getDoctorsInDepartment(
            @PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDoctorsInDepartment(id));
    }

    @DeleteMapping("/{departmentId}/doctors/{doctorId}")
    @Operation(summary = "Remove doctor from department")
    public ResponseEntity<String> removeDoctorFromDepartment(
            @PathVariable Long departmentId,
            @PathVariable Long doctorId) {
        departmentService.removeDoctorFromDepartment(departmentId, doctorId);
        return ResponseEntity.ok("Doctor removed from department successfully!");
    }

    @PutMapping("/{departmentId}/doctors/{doctorId}/set-head")
    @Operation(summary = "Set doctor as head of department")
    public ResponseEntity<DepartmentDoctorDTO> setHeadDoctor(
            @PathVariable Long departmentId,
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(departmentService.setHeadDoctor(departmentId, doctorId));
    }
}
