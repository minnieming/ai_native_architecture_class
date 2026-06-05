package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(
        @NotNull Long employeeId,
        @NotBlank String team
) {
    // 여기서는 toEntity로 받지 않고, service단에서 처리한다.
}
