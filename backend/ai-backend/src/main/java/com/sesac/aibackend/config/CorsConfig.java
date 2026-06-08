package com.sesac.aibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS 설정 (Day 5).
 *
 * React 개발 서버(Vite 기본 포트 5173)의 호출을 허용합니다.
 *
 * 보안 주의:
 * - allowCredentials=true 와 함께 allowedHeaders="*" 사용은 비권장입니다.
 *   필요한 헤더만 화이트리스트로 명시합니다.
 *   4
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of( // 출처 설정
                "http://localhost:5173",
                "http://localhost:3000" // fastapi
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 메서드 적어두기
        config.setAllowedHeaders(List.of( // 헤더를 어떻게 하느냐에 따라 다르다. 밑의 값들은 허용한다는것
                "Authorization",
                "Content-Type", // rag할때 컨텐츠 타입이 들어온다.
                "Accept",
                "X-Requested-With"
        ));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // 이걸 통해서
        source.registerCorsConfiguration("/**", config); // 모든 출처는 이 configuration을 따른다는 것
        return source;
    }
}
