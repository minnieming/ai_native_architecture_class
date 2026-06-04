package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Cosmetic;
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
