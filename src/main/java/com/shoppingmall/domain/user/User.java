package com.shoppingmall.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.inquiry.ItemInquiry;
import com.shoppingmall.domain.inquiry.ItemInquiryAnswer;
import com.shoppingmall.domain.review.ItemReview;
import com.shoppingmall.domain.order.Order;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.dto.UserResponseDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(STRING)
    @Builder.Default
    private UserRole role = UserRole.USER;

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

    @OneToOne(mappedBy = "user", cascade = ALL)
    @JsonIgnore
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<ItemInquiry> itemInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<ItemInquiryAnswer> itemInquiryAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<ItemReview> itemReviews = new ArrayList<>();

    /**
     * 비즈니스 로직
     */
    public void updateProfiles(UserRequestDto userRequestDto){
        this.identifier = userRequestDto.getIdentifier();
        this.password = userRequestDto.getPassword();
        this.username = userRequestDto.getUsername();
        this.email = userRequestDto.getEmail();
        this.address = new Address(userRequestDto.getRoadAddress(), userRequestDto.getDetailAddress());
    }

    public UserResponseDto toUserResponseDto(User user){
        return UserResponseDto.builder()
                .id(user.id)
                .identifier(user.identifier)
                .password(user.password)
                .username(user.username)
                .email(user.email)
                .address(user.address)
                .build();
    }
}
