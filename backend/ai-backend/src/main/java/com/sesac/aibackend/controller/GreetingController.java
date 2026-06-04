package com.sesac.aibackend.controller;

import com.sesac.aibackend.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // 이게 없으면 빈 객체로 등록되지 않는다.
// 의존성 주입 받기
@RequiredArgsConstructor// 생성자 주입 -> final이 붙어 있으면 의존성 주입 받아 온다.
public class GreetingController {

    private final GreetingService greetingService; // 이걸 이렇게 가져오려면, 주입 되는 대상도 bean으로 등록이 되어 있다.

    @GetMapping("/greeting")
    public Map<String, String> greeting(@RequestParam(defaultValue = "World") String name) { // 기본값. 비어있는 값일때 에러가 나지 않도록 requestparam 설정
        return Map.of("message", greetingService.hello(name));
    }
}
