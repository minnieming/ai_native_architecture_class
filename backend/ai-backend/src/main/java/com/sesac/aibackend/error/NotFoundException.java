package com.sesac.aibackend.error;

public class NotFoundException extends RuntimeException { // 클래스를 상속받아서 사용
    public NotFoundException(String message) { // 생성자에 부모 받아야 하니까 있는것
        super(message);
    }

    public static NotFoundException of(String resource, Object id) {
        return new NotFoundException(resource + " not found: " + id);
    }
}
