package com.sesac.aibackend.sp01_hw_cosmetic.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cosmetic {

    private Long id;
    private String category;
    private String name;
    private int price;
}
