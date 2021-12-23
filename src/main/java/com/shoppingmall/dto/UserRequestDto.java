package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.Address;
import com.shoppingmall.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotEmpty
    private String identifier;

    private UserRole role;

    @NotNull
    private String password;

    @NotNull
    private String username;

    @Email
    private String email;

    @NotNull
    private String roadAddress;

    @NotNull
    private String detailAddress;

    public User toEntity(){
        return User.builder()
                .identifier(identifier)
                .password(password)
                .username(username)
                .email(email)
                .address(new Address(roadAddress, detailAddress))
                .build();
    }
}
