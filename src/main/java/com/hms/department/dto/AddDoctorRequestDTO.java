package com.hms.department.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDoctorRequestDTO {
    private Long doctorId;
    private Boolean isHead;
}
