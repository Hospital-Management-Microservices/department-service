package com.hms.department.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDoctorDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private Boolean isHead;
}
