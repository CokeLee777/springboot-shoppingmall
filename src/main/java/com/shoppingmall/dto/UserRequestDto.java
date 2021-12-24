package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.user.Address;
import com.shoppingmall.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank(message = "아이디를 작성해주세요.")
//    @Pattern()
    private String identifier;

    private UserRole role;

    @NotBlank(message = "비밀번호를 작성해주세요.")
//    @Pattern()
    private String password;

    @NotBlank(message = "이름을 작성해주세요.")
    private String username;

    @NotBlank(message = "이메일을 작성해주세요.")
    @Email(message = "이메일 양식으로 작성해주세요.")
    private String email;

    @Size(max = 50, message = "도로명 주소는 최대 50자까지 허용합니다.")
    private String roadAddress;

    @Size(max = 50, message = "상세 주소는 최대 50자까지 허용합니다.")
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
