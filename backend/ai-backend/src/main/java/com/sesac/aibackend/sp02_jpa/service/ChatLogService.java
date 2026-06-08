package com.sesac.aibackend.sp02_jpa.service;

import com.sesac.aibackend.sp02_jpa.domain.ChatLog;
import com.sesac.aibackend.sp02_jpa.domain.User;
import com.sesac.aibackend.sp01_restapi.error.NotFoundException;
import com.sesac.aibackend.sp02_jpa.repository.ChatLogRepository;
import com.sesac.aibackend.sp02_jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 채팅 로그 저장/조회 서비스.
 *
 * 트랜잭션 경계를 서비스 메서드 단위로 명시합니다.
 * 비동기 흐름(Mono)에서 호출되더라도 이 메서드는 동기 트랜잭션에서 실행됩니다.
 */
@Service
@RequiredArgsConstructor
public class ChatLogService {

    private final UserRepository userRepository; // 1:N이기 때문에 여기에도 접근할 수 있다. (필요)
    private final ChatLogRepository chatLogRepository;

    @Transactional
    public ChatLog save(Long userId, String prompt, String response) {
        User user = userRepository.findById(userId) // 이럴땐 부모 아이디를 먼저 매칭 하고 -> 그리고 자식을 처리한다.
                .orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.save(
                ChatLog.builder()
                        .user(user) // 부모 객체를 품어서 저장하기
                        .prompt(prompt)
                        .response(response)
                        .build()
        );
    } // 성공적으로 되면 commit후 닫아.

    /**
     * 기본 조회 — userId(PK)로 조회, fetch join 없음.
     * 응답에서 getUser().getId()만 읽는 from()과 짝을 이룹니다.
     */
    @Transactional(readOnly = true)
    public List<ChatLog> findByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.findByUserIdOrderByCreatedAtDesc(userId); // 이건 쿼리 메소드 (기본 메소드가 아니다)
    }

    /**
     * fetch join 조회 — userId(PK)로 조회하며 user를 함께 로딩.
     * 응답에서 getUser().getUsername()을 읽는 fromWithUsername()과 짝을 이룹니다.
     */
    @Transactional(readOnly = true) // 얘는 연속상태에 있다.
    public List<ChatLog> findByUserIdWithUser(Long userId) { // user와 함께 fetch join 조회
        // 존재하지 않는 사용자는 404로 구분 (fetch join은 결과가 없으면 빈 리스트라 구분 불가)
        userRepository.findById(userId)
                .orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.findByUserIdWithUser(userId); // 얘는 영속상태랑 상관없다. 그래서 lazy ---- 이런 에러?가 나지 않는다.
    }
}
