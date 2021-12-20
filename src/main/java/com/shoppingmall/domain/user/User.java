package com.shoppingmall.domain.user;

import com.shoppingmall.domain.inquiry.ItemInquiry;
import com.shoppingmall.domain.inquiry.ItemInquiryAnswer;
import com.shoppingmall.domain.review.ItemReview;
import com.shoppingmall.domain.order.Order;
import com.shoppingmall.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

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

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ItemInquiry> itemInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ItemInquiryAnswer> itemInquiryAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ItemReview> itemReviews = new ArrayList<>();
}
