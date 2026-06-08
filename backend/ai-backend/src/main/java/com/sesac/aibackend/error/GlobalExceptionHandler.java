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

    @ExceptionHandler(DuplicateException.class) // 이걸 해야 409가 에러만 발생하고 땡이 아니라 핸들링 할 수 있게 해준다.
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // CONFLICT 내고 끝나게 해준다.
                .body(ErrorResponse.of("CONFLICT", e.getMessage()));
    }
}
