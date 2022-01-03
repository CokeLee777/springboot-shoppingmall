package com.shoppingmall.domain.cart;

import com.shoppingmall.domain.cartitem.CartItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.CartItemRequestDto;
import com.shoppingmall.dto.CartRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.CartItemRequestDto.*;
import static com.shoppingmall.dto.CartRequestDto.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(mappedBy = "cart", fetch = LAZY, cascade = ALL)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * 비즈니스 로직
     */
    //상품 추가
    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    //상품 삭제
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
        cartItem.dropItem();
        if(cartItem.getCart() == this) cartItem.setCart(null);
    }

    //배송비 조회
    public Integer getDeliveryPrice(){
        if(getTotalPrice() < 30000) return 2500;
        else return 0;
    }

    //장바구니 전체 가격 조회
    public Integer getTotalPrice(){
        Integer totalPrice = 0;
        for(CartItem cartItem: cartItems){
            totalPrice += cartItem.getTotalPrice();
        }
        return totalPrice;
    }

    public CartInfo toCartInfo(List<CartItemInfo> cartItemInfos){
        return CartInfo.builder()
                .cartItemInfos(cartItemInfos)
                .deliveryPrice(getDeliveryPrice())
                .totalPrice(getTotalPrice())
                .build();
    }

}
