package com.shoppingmall.domain.cartitem;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import lombok.*;

import javax.persistence.*;

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
    private Integer cartPrice;

    @Column
    private Integer itemCount;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder
    private CartItem(Item item, Integer cartPrice, Integer itemCount){
        this.item = item;
        this.cartPrice = cartPrice;
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

    //장바구니 상품 전체 가격조회
    public Integer getTotalPrice(){
        return getCartPrice() * getItemCount();
    }
}
