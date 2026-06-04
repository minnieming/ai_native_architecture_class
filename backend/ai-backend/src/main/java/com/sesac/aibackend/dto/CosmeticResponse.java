package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Cosmetic;

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
