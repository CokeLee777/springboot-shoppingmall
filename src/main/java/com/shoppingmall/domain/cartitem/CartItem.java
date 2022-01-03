package com.shoppingmall.domain.cartitem;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.dto.CartItemRequestDto;
import lombok.*;

import javax.persistence.*;

import static com.shoppingmall.dto.CartItemRequestDto.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

        item.removeStock(itemCount);
    }

    /**
     * 비즈니스 로직
     */
    //장바구니에서 특정 상품 없앨 떄
    public void dropItem(){
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
                .itemImg(item.getItemImg())
                .itemName(item.getName())
                .itemPrice(itemPrice)
                .itemCount(itemCount)
                .build();
    }
}