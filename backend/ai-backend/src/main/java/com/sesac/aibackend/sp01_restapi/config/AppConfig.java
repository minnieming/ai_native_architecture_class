package com.sesac.aibackend.sp01_restapi.config;

import com.sesac.aibackend.sp01_restapi.util.MessageFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 객체를 빈으로 등록 & 설정에 필요한 객체로 만들어준다.
public class AppConfig {

    @Bean
    public MessageFormatter messageFormatter() { // 아까 만든 메세지 포맷터 클래스.
        return new MessageFormatter(); // 그걸 객체로 내보내주는 bean이다.
    }
}
