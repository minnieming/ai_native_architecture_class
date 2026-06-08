package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Role;
import com.sesac.aibackend.domain.User;
import com.sesac.aibackend.dto.LoginRequest;
import com.sesac.aibackend.dto.SignupRequest;
import com.sesac.aibackend.error.DuplicateException;
import com.sesac.aibackend.repository.UserRepository;
import com.sesac.aibackend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 인증 컨트롤러 — /signup, /login.
 *
 * /login 성공 시 JWT를 발급하여 응답합니다.
 *
 * 동시성 주의: existsByUsername 후 save 는 TOCTOU race 위험.
 * users.username UNIQUE 제약과 DataIntegrityViolationException 잡기로 이중 방어합니다.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignupRequest req) {
        if (userRepository.existsByUsername(req.username())) { // 회원가입이므로 회원이 있을때 에러처리
            throw new DuplicateException("username already taken: " + req.username());
        }
        try { // 중복된 사용자가 아니라면 저장
            userRepository.save(User.builder() // 유저 객체를 만들어준다.
                    .username(req.username())
                    .passwordHash(passwordEncoder.encode(req.password())) // 패스워드 인코딩 하기 -> 암호화
                    .role(Role.USER)
                    .build());
        } catch (DataIntegrityViolationException e) {
            // unique 제약 위반 — 동시 가입 시도
            throw new DuplicateException("username already taken: " + req.username());
        }
        return ResponseEntity.status(201).body(Map.of("username", req.username()));
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate( // 검증 단계 -> 내부적으로는 UserDetailsService의 loadUserByUsername 동작
                // SECURITY에서 인증 객체를 관리하는 매니저 authenticationManager -> 얘기 검증할 수 있는게 있다. 우리 사용자인지 검증하는 함수..authenticate
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        ); // 인증성공하면

        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority) // 인증 객체 꺼내오기
                .map(a -> a.replace("ROLE_", ""))
                .orElse("USER"); // 저장할때는 USER, ADMIN으로 저자하는데 SECURITY 내부에서 관례적으로 ROLE_을 붙여서 이렇게 하고, 우리가 사용할때는 빼고한다 (그래서 위에서 넣음)
        String token = jwtUtil.generate(auth.getName(), role); // JWT를 만들어주기
        return Map.of("token", token, "username", auth.getName(), "role", role); // 그걸 내보내주기
    }
}
