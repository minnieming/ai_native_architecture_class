package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Employee;

public record EmployeeResponse(Long id, String name) {

    public static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(employee.getId(), employee.getName());
    }
}