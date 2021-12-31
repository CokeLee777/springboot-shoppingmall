package com.shoppingmall.domain.user;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.inquiry.ItemInquiry;
import com.shoppingmall.domain.inquiry.ItemInquiryAnswer;
import com.shoppingmall.domain.review.ItemReview;
import com.shoppingmall.domain.order.Order;
import com.shoppingmall.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.UserRequestDto.*;
import static com.shoppingmall.dto.UserResponseDto.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

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

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "cart_id")
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

    public void updateProfiles(UserUpdateForm userUpdateForm){
        this.password = userUpdateForm.getPassword();
        this.username = userUpdateForm.getUsername();
        this.email = userUpdateForm.getEmail();
        this.address = new Address(userUpdateForm.getRoadAddress(), userUpdateForm.getDetailAddress());
    }

    public UserProfileInfo toUserProfileInfo(){
        return UserProfileInfo.builder()
                .id(id)
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
