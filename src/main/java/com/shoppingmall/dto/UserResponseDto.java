package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Builder
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String identifier;
    private UserRole role;
    private String username;
    private String email;
    private Address address;

}
