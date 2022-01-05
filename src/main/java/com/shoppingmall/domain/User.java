package com.shoppingmall.domain;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.UserRequestDto.UserUpdateForm;
import static com.shoppingmall.dto.UserResponseDto.UserProfileInfo;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(STRING)
    private UserRole role;

    @Column
    private String identifier;

    @Column
    private String password;

    @Column
    private String username;

    @Column
    private String email;

    @Embedded
    private Address address;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Order> orders = new ArrayList<>();

    @Builder
    private User(String identifier, String password, String username, String email, Address address, Cart cart){
        this.role = UserRole.USER;  //일반 사용자로 초기 셋팅
        this.identifier = identifier;
        this.password = password;
        this.username = username;
        this.email = email;
        this.address = address;
        this.setCart(cart);
    }

    //연관관계 메서드
    //회원을 만들면 장바구니도 함께 생성된다.
    private void setCart(Cart cart){
        this.cart = cart;
        cart.setUser(this);
    }

    public void updateProfiles(UserUpdateForm userUpdateForm){
        this.password = userUpdateForm.getPassword();
        this.username = userUpdateForm.getUsername();
        this.email = userUpdateForm.getEmail();
        this.address = new Address(userUpdateForm.getRoadAddress(), userUpdateForm.getDetailAddress());
    }

    //관리자 계정을 만들 때, 이 메서드를 호출해주어야한다.
    public void toAdminUser(){
        this.role = UserRole.ADMIN;
    }

    public UserProfileInfo toUserProfileInfo(){
        return UserProfileInfo.builder()
                .id(id)
                .role(role)
                .identifier(identifier)
                .password(password)
                .username(username)
                .email(email)
                .address(address)
                .build();
    }

    public UserUpdateForm toUpdateUserForm(){
        return UserUpdateForm.builder()
                .identifier(identifier)
                .password(password)
                .username(username)
                .email(email)
                .roadAddress(address.getRoad())
                .detailAddress(address.getDetail())
                .build();
    }
}
