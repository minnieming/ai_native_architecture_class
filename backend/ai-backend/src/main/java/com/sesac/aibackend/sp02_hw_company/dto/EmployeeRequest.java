package com.sesac.aibackend.sp02_hw_company.dto;

import com.sesac.aibackend.sp02_hw_company.domain.Employee;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRequest(@NotBlank String name) {

    public Employee toEntity() {
        return Employee.builder()
                .name(name)
                .build();
    }
}
