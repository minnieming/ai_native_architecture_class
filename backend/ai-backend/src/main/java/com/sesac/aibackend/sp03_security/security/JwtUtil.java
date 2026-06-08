package com.sesac.aibackend.sp03_security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * JWT 발급과 검증.
 *
 * JJWT 0.12.x fluent API 사용:
 * - 빌더: setSubject() 에서 subject() 로 (setter 접두사 제거)
 * - 파서: parserBuilder() 에서 parser() + verifyWith() 로
 * - 결과: parseClaimsJws().getBody() 에서 parseSignedClaims().getPayload() 로
 */
@Component // 빈으로 등록되어 쓰인다.
public class JwtUtil { // jwt 기능을 보아놓은 곳. jwt가 필요한곳은 이거 주입받아서 사용하면 된다. (컴포넌트 어노테이션 달아놨으니까)

    private static final int MIN_SECRET_BYTES = 32;   // HS256 권장 최소 길이 -> secret key가 너무 짧으면 안되기 때문에 기준을 넣어둔 것

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtUtil( // @value 값은 application.yaml에
            @Value("${jwt.secret}") String secret, // '서명'에 해당하는 것
            @Value("${jwt.expiration-millis}") long expirationMillis) { // 토큰이 살아있을 수 있는 시간
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < MIN_SECRET_BYTES) { // 에러처리
            throw new IllegalStateException(
                    "jwt.secret must be at least " + MIN_SECRET_BYTES + " bytes (current: "
                            + bytes.length + "). Set JWT_SECRET environment variable.");
        }
        this.secretKey = Keys.hmacShaKeyFor(bytes); // 시크릿키 세팅
        this.expirationMillis = expirationMillis; // 만료 세팅
    }

    // 이 util은 두개의 기능이 있다.

    // 1. jwt의 생성시 이 기능을 사용
    public String generate(String username, String role) { // payload에 담기 위해 매개변수 가져오기
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("role", role) // 그외 나머지 속성값을 넣기 위해 넣었다.
                .issuedAt(Date.from(now)) // 토큰 생성일
                .expiration(Date.from(now.plusMillis(expirationMillis))) // 중요 : 토큰이 살아 있을 수 있는 시간. 현재시간보다 초를 더해서 사용.
                .signWith(secretKey) // 서명키 -> 이걸 넣어야 토큰이 위조되지 않는다.
                .compact();
    }

    // 2. jwt를 까보는 것. 검증.
    public Claims parse(String token) throws JwtException { // 토큰을 내보내준다.
        return Jwts.parser() // jwt와 관련된 의존성에서 주는 기능 parser : 검증
                .verifyWith(secretKey) // 서명값을 넣어서 대조해준다.
                .build()
                .parseSignedClaims(token)
                .getPayload(); // 이 클래임은 페이로드만 준다.
    }
}
