package com.sesac.aibackend.sp03_security_rag.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * FastAPI 호출용 WebClient Bean (Day 5).
 *
 * 30초 응답 타임아웃 + 5초 연결 타임아웃을 설정합니다.
 */
@Configuration
public class WebClientConfig { // 파이썬과 비동기 통신시, 이 클라이언트를 쓰겠다고 설정 잡아둔 것.

    @Bean // webclient 관련 메서드를 빈으로 등록
    public WebClient pythonWebClient(@Value("${python.base-url}") String baseUrl) { // baseurl을 잡고 시작
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000) // deadlock에 빠지지 않기 위해서 정해놓기
                .responseTimeout(Duration.ofSeconds(60)); // 제한시간 30초. 이 시간안에 답이 오지 않으면 멈춘다. -> ai의 응답속도에 따라.

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient)) // 이걸 통해서 httpclient를 연결해준다.
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
