package com.sesac.aibackend.sp01_hw_cosmetic.dto;

import com.sesac.aibackend.sp01_hw_cosmetic.domain.Cosmetic;

public record CosmeticResponse(
        Long id,
        String name,
        int price
) {

    public static CosmeticResponse from(Cosmetic cosmetic) {
        return new CosmeticResponse(
                cosmetic.getId(),
                cosmetic.getName(),
                cosmetic.getPrice()
        );
    }
}
