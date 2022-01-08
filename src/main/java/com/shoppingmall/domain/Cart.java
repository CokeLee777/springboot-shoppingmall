package com.shoppingmall.domain;

import com.shoppingmall.domain.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.CartItemResponseDto.CartItemInfo;
import static com.shoppingmall.dto.CartResponseDto.CartInfo;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(mappedBy = "cart", fetch = LAZY)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * 비즈니스 로직
     */
    //상품 장바구니에서 하나 추가
    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    //상품 장바구니에서 하나 삭제
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
        cartItem.cancel();
    }
    //장바구니 비우기
    public void clearCartItems(){
        this.cartItems.clear();
    }

    //배송비 조회
    public Integer getDeliveryPrice(){
        if(getTotalPrice() < 50000) return 2500;
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
                .id(id)
                .cartItemInfos(cartItemInfos)
                .deliveryPrice(getDeliveryPrice())
                .totalPrice(getTotalPrice())
                .build();
    }

}
