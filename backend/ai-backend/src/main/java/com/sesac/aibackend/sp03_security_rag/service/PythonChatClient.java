package com.sesac.aibackend.sp03_security_rag.service;

import com.sesac.aibackend.sp03_security_rag.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * FastAPI(/chat) 호출 클라이언트.
 *
 * 비동기 Mono를 반환하지만 컨트롤러에서 block()으로 동기 호출도 가능합니다.
 * 초보자 친화를 위해 ChatController는 block()으로 시작하고 비동기는 옵션으로 안내합니다.
 */
@Slf4j
@Component // 빈객체가 하나 뜬다?
@RequiredArgsConstructor
public class PythonChatClient {

    private final WebClient pythonWebClient;

    public Mono<ChatResponse> chat(String prompt) { // mono : 하나만 뜨는 애 / request는 프롬포트 모양으로 들어온다 (내용이 그렇다는것)
        Map<String, Object> body = Map.of("prompt", prompt); // 바디에 넣어서
        return pythonWebClient.post() // post로 쏜다
                .uri("/chat") // 8000번의 chat으로 쏜다.
                .bodyValue(body)
                .retrieve() // 뒤에 있는걸 수행할 수 있게 해주는 것. fastapi가 끝나고 콜하는거라고 생각하면 된다.
                .onStatus(HttpStatusCode::is4xxClientError, resp -> {
                    log.warn("python 4xx: {}", resp.statusCode()); // 파이썬에서 400번대 에러가 났다면 이쪽에 넣어주기 -> 에러 처리
                    return Mono.error(new IllegalArgumentException("invalid request to python"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, resp -> {
                    log.error("python 5xx: {}", resp.statusCode());
                    return Mono.error(new RuntimeException("python server error"));
                })
                .bodyToMono(ChatResponse.class); // 모노 객체로 만들어준다.
    }
}
