package com.hms.department.service;

import com.hms.department.dto.*;
import com.hms.department.model.Department;
import com.hms.department.model.DepartmentDoctor;
import com.hms.department.repository.DepartmentDoctorRepository;
import com.hms.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentDoctorRepository departmentDoctorRepository;
    private final RestTemplate restTemplate;

    @Value("${doctor.service.url}")
    private String doctorServiceUrl;

    // ==================== DEPARTMENT CRUD ====================

    // Create Department
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setLocation(request.getLocation());
        department.setContactNumber(request.getContactNumber());
        department.setEmail(request.getEmail());
        department.setTotalBeds(request.getTotalBeds());
        department.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Department saved = departmentRepository.save(department);
        return mapToResponseDTO(saved);
    }

    // Get All Departments
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get Department By ID
    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        return mapToResponseDTO(department);
    }

    // Update Department
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setLocation(request.getLocation());
        department.setContactNumber(request.getContactNumber());
        department.setEmail(request.getEmail());
        department.setTotalBeds(request.getTotalBeds());
        if (request.getIsActive() != null) {
            department.setIsActive(request.getIsActive());
        }

        Department updated = departmentRepository.save(department);
        return mapToResponseDTO(updated);
    }

    // Delete Department
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    // Search Departments by Name
    public List<DepartmentResponseDTO> searchDepartments(String name) {
        return departmentRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get Active Departments
    public List<DepartmentResponseDTO> getActiveDepartments() {
        return departmentRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================== DOCTOR MANAGEMENT ====================

    // Add Doctor to Department (Inter-service communication)
    public DepartmentDoctorDTO addDoctorToDepartment(Long departmentId, AddDoctorRequestDTO request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));

        // Check if doctor already exists in department
        if (departmentDoctorRepository.existsByDepartmentIdAndDoctorId(departmentId, request.getDoctorId())) {
            throw new RuntimeException("Doctor already exists in this department!");
        }

        // Fetch doctor details from Doctor Service via API Gateway
        DoctorResponseDTO doctorResponse = null;
        try {
            doctorResponse = restTemplate.getForObject(
                    doctorServiceUrl + "/" + request.getDoctorId(),
                    DoctorResponseDTO.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Doctor not found with id: " + request.getDoctorId());
        }

        // If this doctor is head, remove existing head first
        if (Boolean.TRUE.equals(request.getIsHead())) {
            departmentDoctorRepository.findByDepartmentIdAndIsHeadTrue(departmentId)
                    .ifPresent(existingHead -> {
                        existingHead.setIsHead(false);
                        departmentDoctorRepository.save(existingHead);
                    });
        }

        // Create new department doctor
        DepartmentDoctor departmentDoctor = new DepartmentDoctor();
        departmentDoctor.setDepartment(department);
        departmentDoctor.setDoctorId(request.getDoctorId());
        departmentDoctor.setDoctorName(doctorResponse.getFullName());
        departmentDoctor.setSpecialization(doctorResponse.getSpecialization());
        departmentDoctor.setIsHead(request.getIsHead() != null ? request.getIsHead() : false);

        DepartmentDoctor saved = departmentDoctorRepository.save(departmentDoctor);
        return mapToDoctorDTO(saved);
    }

    // Get All Doctors in Department
    public List<DepartmentDoctorDTO> getDoctorsInDepartment(Long departmentId) {
        departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));

        return departmentDoctorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToDoctorDTO)
                .collect(Collectors.toList());
    }

    // Remove Doctor from Department
    public void removeDoctorFromDepartment(Long departmentId, Long doctorId) {
        DepartmentDoctor departmentDoctor = departmentDoctorRepository
                .findByDepartmentIdAndDoctorId(departmentId, doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found in this department!"));

        departmentDoctorRepository.delete(departmentDoctor);
    }

    // Set Head Doctor
    public DepartmentDoctorDTO setHeadDoctor(Long departmentId, Long doctorId) {
        // Remove existing head
        departmentDoctorRepository.findByDepartmentIdAndIsHeadTrue(departmentId)
                .ifPresent(existingHead -> {
                    existingHead.setIsHead(false);
                    departmentDoctorRepository.save(existingHead);
                });

        // Set new head
        DepartmentDoctor departmentDoctor = departmentDoctorRepository
                .findByDepartmentIdAndDoctorId(departmentId, doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found in this department!"));

        departmentDoctor.setIsHead(true);
        DepartmentDoctor saved = departmentDoctorRepository.save(departmentDoctor);
        return mapToDoctorDTO(saved);
    }

    // ==================== HELPER METHODS ====================

    private DepartmentResponseDTO mapToResponseDTO(Department department) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setLocation(department.getLocation());
        dto.setContactNumber(department.getContactNumber());
        dto.setEmail(department.getEmail());
        dto.setTotalBeds(department.getTotalBeds());
        dto.setIsActive(department.getIsActive());
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());

        if (department.getDoctors() != null) {
            dto.setDoctors(department.getDoctors()
                    .stream()
                    .map(this::mapToDoctorDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private DepartmentDoctorDTO mapToDoctorDTO(DepartmentDoctor dd) {
        DepartmentDoctorDTO dto = new DepartmentDoctorDTO();
        dto.setId(dd.getId());
        dto.setDoctorId(dd.getDoctorId());
        dto.setDoctorName(dd.getDoctorName());
        dto.setSpecialization(dd.getSpecialization());
        dto.setIsHead(dd.getIsHead());
        return dto;
    }
}
