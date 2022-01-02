package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserProfileInfo {
        private Long id;
        private UserRole role;
        private String identifier;
        private String password;
        private String username;
        private String email;
        private Address address;
    }

}
