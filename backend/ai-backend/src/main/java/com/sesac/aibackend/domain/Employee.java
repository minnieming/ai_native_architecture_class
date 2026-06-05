package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder // 빌더넣고, @AllArgsConstructor 이게 아니라 @RequiredArgsConstructor 이거 넣으니까 에러가 났다. -> 확인해보기!
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
