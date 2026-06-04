package com.sesac.aibackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// 여기에 빈을 등록해줘야. 컴포넌트 스캔때 스캔할 수 있으니까 어노테이션으로 해주기.
// 이건 기능도 수행하는 특별한 빈.
@RestController
public class healthController {

    @GetMapping("health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
