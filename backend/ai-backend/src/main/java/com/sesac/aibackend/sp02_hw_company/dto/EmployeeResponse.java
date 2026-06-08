package com.sesac.aibackend.sp02_hw_company.dto;

import com.sesac.aibackend.sp02_hw_company.domain.Employee;

public record EmployeeResponse(Long id, String name) {

    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(employee.getId(), employee.getName());
    }
}