package com.hms.department.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// DTO for Department Request (Create/Update)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDTO {
    private String name;
    private String description;
    private String location;
    private String contactNumber;
    private String email;
    private Integer totalBeds;
    private Boolean isActive;
}
