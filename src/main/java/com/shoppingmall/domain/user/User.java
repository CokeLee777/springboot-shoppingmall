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

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(mappedBy = "user", cascade = ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<ItemInquiry> itemInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<ItemInquiryAnswer> itemInquiryAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<ItemReview> itemReviews = new ArrayList<>();

    /**
     * 비즈니스 로직
     */
    public User updatePassword(String password){
        this.password = password;

        return this;
    }
}
