package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username); // 반환값은 공식문서 같은거 보고 봐야한다. 뭘 하느냐에 따라서 반환값이 다르다.

    boolean existsByUsername(String username);

    /** 소셜 로그인 신원 조회 — (provider, providerId) 조합이 사용자의 안정적 식별 키입니다. */
    Optional<User> findByProviderAndProviderId(String provider, String providerId); // and로 묶은것

}
