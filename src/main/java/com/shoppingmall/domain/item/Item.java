package com.shoppingmall.domain.item;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.inquiry.ItemInquiry;
import com.shoppingmall.domain.review.ItemReview;
import com.shoppingmall.domain.orderitem.OrderItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.dto.ItemResponseDto;
import com.shoppingmall.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private Integer stockQuantity;

    @Enumerated(value = STRING)
    private ItemStatus itemStatus;

    @Column
    private String itemImg;

    @OneToMany(mappedBy = "item", cascade = ALL)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = ALL)
    @Builder.Default
    private List<ItemInquiry> itemInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = ALL)
    @Builder.Default
    private List<ItemReview> itemReviews = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = ALL)
    @Builder.Default
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

    /**
     * 비즈니스 로직
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;

        if(restStock < 0){
            throw new NotEnoughStockException("재고가 부족합니다.");
        }

        this.stockQuantity = restStock;
    }

    public ItemResponseDto toItemResponseDto(Item item){
        return ItemResponseDto.builder()
                .name(item.name)
                .price(item.price)
                .stockQuantity(item.stockQuantity)
                .itemStatus(item.itemStatus)
                .itemImg(item.itemImg)
                .build();
    }
}
