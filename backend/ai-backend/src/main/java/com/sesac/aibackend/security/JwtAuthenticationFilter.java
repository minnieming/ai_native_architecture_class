package com.sesac.aibackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 요청에서 1회 실행되어 Authorization 헤더의 JWT를 검증합니다.
 *
 * 검증 성공 시 UserDetails 를 principal 로 세팅하여 컨트롤러에서
 * {@code @AuthenticationPrincipal UserDetails user} 로 받을 수 있도록 합니다.
 * (Form 로그인 경로와 principal 타입 일관성을 유지합니다.)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 토큰 검증 -> SECURITY CONTEXT에 인증정보 넣어서 만들기

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer "; // 신원 인증할때 쓰는 방법

    private final JwtUtil jwtUtil; // 생성, 검증이 있는 객체
    private final UserDetailsService userDetailsService; // SECURITY만 쓰는걸 구현체로 만들었기 때문에 사용한다.

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response, // 이 필터 체인들을 가져와서 쓴다. 여기있는게 필터 체인들.
                                     FilterChain chain) throws ServletException, IOException { // 여기서 필터 체인을 시작하게 한다.
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX)) { // 헤더에서 들어올때 PRIFIX로 시작하는지 본다 -> 정상토큰인지 확인
            String token = header.substring(PREFIX.length()); // BEARER 빼기
            try {
                Claims claims = jwtUtil.parse(token); // 토큰을 검증한다.
                String username = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 인증정보 담아서 객체를 만든다 -> CONTEXT HOLDER에 넣기 위해서 (USERDETAILSSERVICEIMPL 클래스 확인)

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth); // 이걸 contextholder로 인증객체로 만들어준다. (이 유저객체는 security에서 사용하는거다)
            } catch (JwtException e) {
                log.debug("JWT verification failed: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            } catch (UsernameNotFoundException e) {
                log.debug("user from JWT not found: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response); // 필터를 수행하기
    }
}
