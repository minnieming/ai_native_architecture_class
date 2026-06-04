package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Cosmetic;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CosmeticRequest(
        @NotBlank String sort,
        @NotBlank String name,
        @Min(0) int price
) {

    public Cosmetic toEntity() {
        return Cosmetic.builder()
                .sort(sort)
                .name(name)
                .price(price)
                .build();
    }
}
