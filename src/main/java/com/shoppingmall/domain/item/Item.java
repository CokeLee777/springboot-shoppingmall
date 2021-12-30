package com.shoppingmall.domain.item;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.inquiry.ItemInquiry;
import com.shoppingmall.domain.review.ItemReview;
import com.shoppingmall.domain.orderitem.OrderItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.dto.ItemRequestDto;
import com.shoppingmall.dto.ItemResponseDto;
import com.shoppingmall.exception.NotEnoughStockException;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.ItemResponseDto.*;
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

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_category_id")
    public ItemCategory itemCategory;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /**
     * 연관관계 메서드
     */
    public void setItemCategory(ItemCategory itemCategory){
        if(this.itemCategory != null){
            this.itemCategory.getItems().remove(this);
        }
        this.itemCategory = itemCategory;
        itemCategory.getItems().add(this);
    }

    public void updateItem(ItemRequestDto itemRequestDto){
        this.name = itemRequestDto.getName();
        this.price = itemRequestDto.getPrice();
        this.stockQuantity = itemRequestDto.getStockQuantity();
        this.itemStatus = itemRequestDto.getItemStatus();
        this.itemImg = itemRequestDto.getItemImg();
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

    public ItemInfo toItemInfo(){
        return ItemInfo.builder()
                .id(id)
                .itemCategoryId(itemCategory.getId())
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .itemStatus(itemStatus)
                .itemImg(itemImg)
                .build();
    }
}
