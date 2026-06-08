package com.sesac.aibackend.sp01_restapi.dto;

import com.sesac.aibackend.sp01_restapi.domain.Item;

public record ItemResponse(Long id, String name, int price) { // 응답 객체

    public static ItemResponse from(Item item) { // 생성자
        return new ItemResponse(item.getId(), item.getName(), item.getPrice());
    }
}
