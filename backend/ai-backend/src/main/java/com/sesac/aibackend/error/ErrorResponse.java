package com.sesac.aibackend.error;

import java.time.Instant;

public record ErrorResponse(String code, String message, Instant timestamp) { // instant 시간 스탬프를 찍기 위해서

    public static ErrorResponse of(String code, String message) { // method
        return new ErrorResponse(code, message, Instant.now());
    }
}
