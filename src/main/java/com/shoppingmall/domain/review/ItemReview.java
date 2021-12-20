package com.shoppingmall.domain.review;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemReview extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_review_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_id")
    private Item item;
}
