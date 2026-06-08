package com.sesac.aibackend.sp03_security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration // 객체를 빈으로 등록하면서 설정하는 것
@EnableWebSecurity // 이게 중요! -> security 체인을 관장 (.authorizeHttpRequests 이런거 처리) 이게 있어서 security 설정을 할 수 잇따.
@EnableMethodSecurity
@RequiredArgsConstructor // 생성자 주입을 FINAL 로 할 수 잇게 만드는것
public class SecurityConfig {

    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource; // 이거 하면 브라우저 내에서도 cors 처리가 되서 된다.
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler; // 만들었던 핸들로 주입받아오기

    @Bean // dispatcher servlet이 api요청을 넘기기 전에 이 scrutiry를 거치게 하는 것.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // SECURITY에서 HTTP 요청이 들어올때 HTTP 객체를 통째로 가져온다.
        http // HTTP에 여러가지 체인을 걸어준다.
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // 우리는 8080인데 요청은 5173으로 들어오기 때문에 cors 에러가 난거다.
                .csrf(AbstractHttpConfigurer::disable) // 쿠키, 세션을 쓰지 않으니까 끄기
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // STATELESS : 서버에서 관리 안하겠다는 뜻
                // 인증 실패(401) + 권한 부족(403)을 모두 ErrorResponse JSON 표준 포맷으로 통일
                .exceptionHandling(e -> e // EXCEPTION도 여러개 있는데, 아래에 에러들을 나열해주면 된다. -> 핸들러를 붙여서 직접 관리
                        // 지금 현재로썬 에러만 놓고, SECURITY가 적용되지는 않았음
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth // 인가 부분 (권한) 요청을 할때 접근하는걸 설정해줄 수 있다.
                        // 인증 불필요. /error는 403 에러 포워드가 막혀 401로 덮이지 않도록 개방
                        .requestMatchers( // auth안에 매치가 있다. (엔드포인트만 가지고 본다)
                                "/login", "/signup", "/health",
                                "/oauth2/**", "/login/oauth2/**",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                "/h2-console/**", "/error"
                        ).permitAll() // 해당 엔드포인트가 들어올때 정책은 여기에 넣어둔다.
                        // Day 2 인메모리 CRUD 학습용 (운영 권장 X)
                        .requestMatchers("/legacy/items/**").permitAll() // 한번에 넣을수도 있고, 이렇게 별도로 나눌 수도 있다.
                        // 관리자 전용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 해당 엔드포인트로 들어오는건 인가처리를 한다 -> 이 인가로 들어왔을때만 해당 엔드포인트를 허용한다는것.
                        // 그 외 모두 인증 필요 (Day 3 JPA CRUD, /chat 등)
                        .anyRequest().authenticated() // 놓친 API들을 위해, 그외 모든 요청은 전부 인증을 거쳐야 한아고 해놓은것.
                )
                // 구글 OAuth2 로그인 — 성공 시 핸들러가 앱 JWT를 발급 (Day 4 B7/B8)
                // 인가 시작: GET /oauth2/authorization/google, 콜백: /login/oauth2/code/google (자동)
                // 처음엔 .oauth2Login(Customizer.withDefaults()) 이걸로 합니다. 핸들러는 다음 파트에서 진행됩니다.
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2LoginSuccessHandler)) // 얘는 순서는 상관없고, 넣기만 하면 된다. 성공을 하면 해당 핸들러로 간다. (성공 핸들러가 사용자 조회 / 생성 할때 채가서 한다.
                // 이거 분기처리는 핸들러 쪽에서 하면 된다.

                // H2 콘솔 사용을 위한 헤더 완화 (개발 프로파일만)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // 아이프레임 위에서 동작하기 위해서 넣어준 것. (h2가 그래서 그런건가?)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // 해당 필터를 여기에 넣어준다.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // 비밀번호 인코딩하는
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
