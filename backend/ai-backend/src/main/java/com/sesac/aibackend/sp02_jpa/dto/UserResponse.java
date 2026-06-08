package com.sesac.aibackend.sp02_jpa.dto;

import com.sesac.aibackend.sp02_jpa.domain.Role;
import com.sesac.aibackend.sp02_jpa.domain.User;

/**
 * 사용자 응답 DTO.
 *
 * passwordHash는 어떤 경우에도 노출하지 않습니다.
 */
public record UserResponse(Long id, String username, Role role) {

    public static UserResponse from(User user) { // user를 이 객체 형태로 보내주는 것.
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
