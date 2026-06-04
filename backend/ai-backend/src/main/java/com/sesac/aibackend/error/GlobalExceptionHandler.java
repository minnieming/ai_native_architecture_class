package com.sesac.aibackend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 전역 처리기
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class) // notfoundexception이 붙었기 때문에, 저 에러가 나면 아래에 있는걸로 받아서 처리하는 것. 404는 자원이 없는 것
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) { // 에러가 발생하면 받아온다.
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // status. 프론트와는 상태코드로 소통할꺼다.
                .body(ErrorResponse.of("NOT_FOUND", e.getMessage()));
    }
}
