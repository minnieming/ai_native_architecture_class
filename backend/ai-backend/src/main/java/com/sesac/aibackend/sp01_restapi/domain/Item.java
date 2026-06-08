package com.sesac.aibackend.sp01_restapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity // entity 객체는 id가 꼭 필요하기 때문에, 없다면 에러가 뜰수도 있다.
@Table(name = "items") // 기본 예약어로 잡히는 이름을 테이블로 하고 싶을때 사용하면 가능하다.
@Getter
@Setter
@NoArgsConstructor // 매개변수가 없는 생성자를 만들어 준다.
@AllArgsConstructor // 모든 필드(매개변수)를 받는 생성자
@Builder // 객체를 만들어주는 것 (객체 생성 패턴)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id의 생성 전략. (생성 전략이 여러개 있다.)
    private Long id;

    @Column(nullable = false, length = 100) // 없어도 되지만, 제약조건을 넣기 위해 넣었다
    private String name;

    @Column(nullable = false)
    private int price;
}
