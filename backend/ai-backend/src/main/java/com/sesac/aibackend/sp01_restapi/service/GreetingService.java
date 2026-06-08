package com.sesac.aibackend.sp01_restapi.service;

import com.sesac.aibackend.sp01_restapi.util.MessageFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 필수 객체(생성자 객체)는 넣어라.
// 의존성 주입 중 생성자를 넣어주는 어노테이션
public class GreetingService {

    private final MessageFormatter formatter; // 위의 어노테이션으로 생성자 주입을 해준다. 그래서 new 하지 않고 가져와서 쓸 수 있다.
    // 다른 위치에 있는 클래스를 바로 가져와서 쓸 수 있는게 -> bean에 객체로 등록했기 때문이다.

    public String hello(String name) {
        return formatter.format(name);
    }
}
