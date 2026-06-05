package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Department;
import com.sesac.aibackend.domain.Team;

public record DepartmentResponse(
        Long id,
        Long employeeId,
        String employeeName,
        Team team
) {

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getEmployee().getId(),
                null,
                department.getTeam()
        );
    }

    public static DepartmentResponse fromWithName(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getEmployee().getId(),
                department.getEmployee().getName(),
                department.getTeam()
        );
    }
}
