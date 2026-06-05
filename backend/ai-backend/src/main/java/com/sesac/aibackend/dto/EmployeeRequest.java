package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Employee;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRequest(@NotBlank String name) {

    public Employee toEntity() {
        return Employee.builder()
                .name(name)
                .build();
    }
}
