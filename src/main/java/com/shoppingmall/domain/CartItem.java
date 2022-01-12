package com.shoppingmall.domain;

import com.shoppingmall.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.shoppingmall.dto.CartItemResponseDto.CartItemInfo;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class CartItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @Column
    private Integer itemPrice;

    @Column
    private Integer itemCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder
    private CartItem(Item item, Integer itemPrice, Integer itemCount){
        this.item = item;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        //상품을 추가하면 재고 감소
        item.removeStock(itemCount);
    }

    /**
     * 비즈니스 로직
     */
    //장바구니에서 특정 상품 없앨 떄
    public void cancel(){
        getItem().addStock(itemCount);
    }

    //장바구니 상품 하나 총 가격 조회
    public Integer getTotalPrice(){
        return getItemPrice() * getItemCount();
    }

    public CartItemInfo toCartItemInfo(){
        return CartItemInfo
                .builder()
                .id(id)
                .itemImgUrl(item.getItemImgUrl())
                .itemName(item.getName())
                .itemPrice(itemPrice)
                .itemCount(itemCount)
                .build();
    }

}
