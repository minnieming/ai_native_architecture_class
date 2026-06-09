package com.sesac.aibackend.sp01_restapi.error;

import com.sesac.aibackend.sp03_security.error.DuplicateException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리기.
 *
 * 모든 예외를 표준 ErrorResponse 포맷으로 변환합니다.
 *
 * 매핑:
 * - 400: MethodArgumentNotValidException, ConstraintViolationException, IllegalArgumentException,
 *        HttpMessageNotReadableException(잘못된 JSON·enum 값 등 역직렬화 실패)
 * - 401: AuthenticationException (RestAuthenticationEntryPoint도 동일 포맷)
 * - 403: AccessDeniedException
 * - 404: NotFoundException
 * - 409: DuplicateException
 * - 500: Exception
 */
@Slf4j
@RestControllerAdvice // 전역 처리기
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("validation failed");
        return ResponseEntity.badRequest().body(ErrorResponse.of("VALIDATION_FAILED", msg));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        // 잘못된 JSON 형식 또는 enum 등 허용 값 외 입력(예: role="FOO")이 여기로 들어옵니다.
        // 내부 파서 메시지를 그대로 노출하지 않고 일반화된 메시지로 응답합니다.
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("VALIDATION_FAILED", "malformed or invalid request body"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of("CONSTRAINT_VIOLATION", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.of("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(com.sesac.aibackend.sp01_restapi.error.NotFoundException.class) // notfoundexception이 붙었기 때문에, 저 에러가 나면 아래에 있는걸로 받아서 처리하는 것. 404는 자원이 없는 것
    public ResponseEntity<com.sesac.aibackend.sp01_restapi.error.ErrorResponse> handleNotFound(com.sesac.aibackend.sp01_restapi.error.NotFoundException e) { // 에러가 발생하면 받아온다.
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // status. 프론트와는 상태코드로 소통할꺼다.
                .body(com.sesac.aibackend.sp01_restapi.error.ErrorResponse.of("NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(DuplicateException.class) // 이걸 해야 409가 에러만 발생하고 땡이 아니라 핸들링 할 수 있게 해준다.
    public ResponseEntity<com.sesac.aibackend.sp01_restapi.error.ErrorResponse> handleDuplicate(DuplicateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // CONFLICT 내고 끝나게 해준다.
                .body(com.sesac.aibackend.sp01_restapi.error.ErrorResponse.of("CONFLICT", e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("UNAUTHORIZED", e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of("FORBIDDEN", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("unexpected", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of("INTERNAL", e.getMessage()));
    }
}
