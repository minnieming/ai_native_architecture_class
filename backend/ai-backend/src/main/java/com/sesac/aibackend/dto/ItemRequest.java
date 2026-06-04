package com.sesac.aibackend.dto;

import com.sesac.aibackend.domain.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
        @NotBlank String name, // 빈공백을 넣지 말라는 것
        @Min(0) int price
) {

    public Item toEntity() {
        return Item.builder().name(name).price(price).build(); // 빌더로 객체 만들기
    }
}
