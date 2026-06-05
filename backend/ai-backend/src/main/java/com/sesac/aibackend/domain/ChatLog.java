package com.sesac.aibackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_logs") // 스네이크 형식
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // one쪽을 바라보는 것. lazy : 처음에는 프록시 객체로 껴놨다가 필요할때 객체로 한다. -> 이것 때문에 발생하는 문제가 있다.
    @JoinColumn(name = "user_id", nullable = false) // 위랑 이건 짝꿍. 반드시 같이 가야한다.
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String response;

    @CreationTimestamp // 처리방법을 위해 이 어노테이션을 사용.
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // db와 다른 데이터타입 객체.
}
