package com.sesac.aibackend.sp03_security_rag.controller;

import com.sesac.aibackend.sp02_jpa.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 관리자 전용 사용자 관리 라우트 (Day 5 역할 권한 시연).
 *
 * SecurityConfig의 requestMatchers("/admin/**").hasRole("ADMIN")와
 * 메서드 단의 @PreAuthorize("hasRole('ADMIN')") 양쪽으로 이중 보호합니다.
 *
 * 시드 계정: admin / admin1234 (DataInitializer, dev 프로파일 한정)
 */
@RestController // restapi를 제공하는 빈객체
@RequestMapping("/admin")
@RequiredArgsConstructor // 의존성을 생성자 주입으로 받게 해주는 것
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')") // securityconfig에 @EnableMethodSecurity이게 있어야 사용할 수 있다. 권한 한번 더 확인
    @GetMapping("/users")
    public List<Map<String, Object>> listUsers() {
        return userRepository.findAll().stream()
                .map(user -> Map.<String, Object>of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getRole().name(),
                        "provider", user.getProvider()
                ))
                .toList();
    }
}
