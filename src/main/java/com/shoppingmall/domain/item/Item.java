package com.shoppingmall.domain.item;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.inquiry.ItemInquiry;
import com.shoppingmall.domain.review.ItemReview;
import com.shoppingmall.domain.orderitem.OrderItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.ItemStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column
    private String name;

    @Column
    private int price;

    @Enumerated(value = STRING)
    private ItemStatus itemStatus;

    @Column
    private String itemImg;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<ItemInquiry> itemInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<ItemReview> itemReviews = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    public List<ItemCategory> itemCategories = new ArrayList<>();

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /**
     * 연관관계 메서드
     */
    public void addItemCategories(ItemCategory itemCategory){
        this.itemCategories.add(itemCategory);
        itemCategory.setItem(this);
    }
}
