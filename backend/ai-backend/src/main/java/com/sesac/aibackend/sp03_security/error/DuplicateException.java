package com.sesac.aibackend.sp03_security.error;

/**
 * 중복 자원 생성 시도 시 발생 (예: 이미 사용 중인 username).
 *
 * GlobalExceptionHandler에서 409로 매핑됩니다.
 */
public class DuplicateException extends RuntimeException { // 409번을 위한 커스텀에러. 409로 매핑되게끔 만든 exception

    public DuplicateException(String message) {
        super(message);
    }
}
