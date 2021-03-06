package com.shoppingmall.dto;

import com.shoppingmall.domain.Address;
import com.shoppingmall.domain.Cart;
import com.shoppingmall.domain.User;
import com.shoppingmall.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserCreateForm {

        @NotBlank(message = "아이디를 작성해주세요.")
        @Pattern(
                regexp = "^[A-Za-z0-9]{6,12}$",
                message = "아이디는 숫자, 문자 포함의 6~12자 이내로 작성해주세요."
        )
        private String identifier;

        @NotBlank(message = "비밀번호를 작성해주세요.")
        @Pattern(
                regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
                message = "비밀번호는 숫자, 문자, 특수문자 포함의 8~15자 이내로 작성해주세요."
        )
        private String password;

        @NotBlank(message = "이름을 작성해주세요.")
        @Size(max = 10, message = "이름은 10자 이내로 작성해주세요.")
        private String username;

        @NotBlank(message = "이메일을 작성해주세요.")
        @Email(message = "이메일 양식으로 작성해주세요.")
        private String email;

        @NotBlank(message = "도로명 주소를 작성해주세요.")
        @Size(max = 50, message = "도로명 주소는 최대 50자까지 허용합니다.")
        private String roadAddress;

        @NotBlank(message = "상세 주소를 작성해주세요.")
        @Size(max = 50, message = "상세 주소는 최대 50자까지 허용합니다.")
        private String detailAddress;

        public User toEntity(){

            return User.builder()
                    .identifier(identifier)
                    .password(password)
                    .username(username)
                    .email(email)
                    .address(new Address(roadAddress, detailAddress))
                    .cart(new Cart())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserUpdateForm {

        private String identifier;

        @NotBlank(message = "비밀번호를 작성해주세요.")
        @Pattern(
                regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
                message = "비밀번호는 숫자, 문자, 특수문자 포함의 8~15자 이내로 작성해주세요."
        )
        private String password;

        @NotBlank(message = "이름을 작성해주세요.")
        @Size(max = 10, message = "이름은 10자 이내로 작성해주세요.")
        private String username;

        @NotBlank(message = "이메일을 작성해주세요.")
        @Email(message = "이메일 양식으로 작성해주세요.")
        private String email;

        @NotBlank(message = "도로명 주소를 작성해주세요.")
        @Size(max = 50, message = "도로명 주소는 최대 50자까지 허용합니다.")
        private String roadAddress;

        @NotBlank(message = "상세 주소를 작성해주세요.")
        @Size(max = 50, message = "상세 주소는 최대 50자까지 허용합니다.")
        private String detailAddress;
    }

    @Builder
    @Getter @Setter
    @AllArgsConstructor
    public static class LoginUserForm {
        private UserRole role;
        @NotBlank(message = "아이디를 작성해주세요.")
        private String identifier;
        @NotBlank(message = "비밀번호를 작성해주세요.")
        private String password;
    }
}
