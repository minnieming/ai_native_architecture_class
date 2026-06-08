package com.sesac.aibackend.sp01_hw_cosmetic.dto;

import com.sesac.aibackend.sp01_hw_cosmetic.domain.Cosmetic;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CosmeticRequest(
        @NotBlank String category,
        @NotBlank String name,
        @Min(0) int price
) {

    public Cosmetic toEntity() {
        return Cosmetic.builder()
                .category(category)
                .name(name)
                .price(price)
                .build();
    }
}
