package com.shoppingmall.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("일반 사용자"), ADMIN("관리자");

    private String role;
}
