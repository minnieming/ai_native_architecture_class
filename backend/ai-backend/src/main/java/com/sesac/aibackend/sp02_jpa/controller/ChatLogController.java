package com.sesac.aibackend.sp02_jpa.controller;

import com.sesac.aibackend.sp02_jpa.domain.ChatLog;
import com.sesac.aibackend.sp02_jpa.dto.ChatLogRequest;
import com.sesac.aibackend.sp02_jpa.dto.ChatLogResponse;
import com.sesac.aibackend.sp02_jpa.service.ChatLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chat-logs")
@RequiredArgsConstructor
public class ChatLogController {
    // 이제 계속 영속된 객체, 1차 캐시 이렇게 생각해야한다. 테이블에 있다고 생각하면 안됨. 그럼 꼬일수 있으니까!
    private final ChatLogService chatLogService;

    /**
     * [getId 버전] userId(PK)로 대화 로그를 최신순으로 조회합니다.
     * fetch join 없이 from()을 사용 → username은 응답에 null.
     * 예) GET /chat-logs?userId=1
     */
    @GetMapping
    public List<ChatLogResponse> list(@RequestParam Long userId) { // 전체 조회
        return chatLogService.findByUserId(userId).stream()
                .map(ChatLogResponse::from)
                .toList();
    }

    /**
     * [getName 버전] userId(PK)로 조회하며 fetch join으로 user를 함께 로딩해 username까지 응답합니다.
     * 트랜잭션이 닫힌 뒤에도 getUser().getUsername() 접근이 안전합니다.
     * 예) GET /chat-logs/with-user?userId=1
     */
    @GetMapping("/with-user/{userId}")
    public List<ChatLogResponse> listWithUser(@PathVariable Long userId) {
        return chatLogService.findByUserIdWithUser(userId).stream()
                .map(ChatLogResponse::fromWithUsername)
                .toList();
    }

//    사용하지 않음
//    @PostMapping
//    public ResponseEntity<ChatLogResponse> create(@Valid @RequestBody ChatLogRequest req) {
//        ChatLog saved = chatLogService.save(req.userId(), req.prompt(), req.response());
//        URI location = URI.create("/chat-logs/" + saved.getId());
//        return ResponseEntity.created(location).body(ChatLogResponse.from(saved));
//    }
}
