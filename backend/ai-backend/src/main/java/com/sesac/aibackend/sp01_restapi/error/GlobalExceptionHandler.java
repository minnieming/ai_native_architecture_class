package com.sesac.aibackend.sp01_restapi.error;

import com.sesac.aibackend.sp03_security.error.DuplicateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 전역 처리기
public class GlobalExceptionHandler {

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
}
