package com.sesac.aibackend.sp02_hw_company.dto;

import com.sesac.aibackend.sp02_hw_company.domain.Department;
import com.sesac.aibackend.sp02_hw_company.domain.Team;

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
