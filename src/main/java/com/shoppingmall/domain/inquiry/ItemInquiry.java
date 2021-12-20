package com.shoppingmall.domain.inquiry;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInquiry extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_inquiry_id")
    private Long id;

    @Column
    private String message;

    @Column
    private Boolean answerState;

    @Column
    private int answerCount;

    @OneToMany(mappedBy = "itemInquiry")
    private List<ItemInquiryAnswer> itemInquiryAnswers = new ArrayList<>();

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_id")
    private Item item;
}
