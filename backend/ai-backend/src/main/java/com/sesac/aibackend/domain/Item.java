package com.sesac.aibackend.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 매개변수가 없는 생성자를 만들어 준다.
@AllArgsConstructor // 모든 필드(매개변수)를 받는 생성자
@Builder // 객체를 만들어주는 것 (객체 생성 패턴)
public class Item {

    private Long id;
    private String name;
    private int price;
}
