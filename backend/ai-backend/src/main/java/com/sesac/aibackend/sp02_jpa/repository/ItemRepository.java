package com.sesac.aibackend.sp02_jpa.repository;

import com.sesac.aibackend.sp01_restapi.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

// 어노테이션을 달지 않아도 스프링부터 jpa가 알아서 빈으로 등록해서 사용하게 한다.
public interface ItemRepository extends JpaRepository<Item, Long> { // 원래는 implements로 구현체를 써야한다. 하지만 여기선 extends로 jpa가 구현체를 관리해준다. // <엔티티 객체, 아이디 타입>
}
