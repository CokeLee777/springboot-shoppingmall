package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String identifier;
    private String password;
    private String username;
    private String email;
    private Address address;

    public UserRequestDto toUserRequestDto(){
        return UserRequestDto.builder()
                .identifier(identifier)
                .password(password)
                .username(username)
                .email(email)
                .roadAddress(address.getRoad())
                .detailAddress(address.getDetail())
                .build();
    }
}
