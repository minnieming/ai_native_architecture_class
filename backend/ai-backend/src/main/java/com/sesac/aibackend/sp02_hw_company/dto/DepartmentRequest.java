package com.sesac.aibackend.sp02_hw_company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(
        @NotNull Long employeeId,
        @NotBlank String team
) {
    // 여기서는 toEntity로 받지 않고, service단에서 처리한다.
}
