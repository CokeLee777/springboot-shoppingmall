package com.shoppingmall.domain.cart;

import com.shoppingmall.domain.cartitem.CartItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.CartItemResponseDto.*;
import static com.shoppingmall.dto.CartResponseDto.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

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

//    @Builder
//    private Cart(User user, CartItem ... cartItems){
//        this.setUser(user);
//        for (CartItem cartItem : cartItems) {
//            this.addCartItem(cartItem);
//        }
//    }

    /**
     * 연관관계 메서드
     */
//    public void setUser(User user){
//        this.user = user;
//        user.setCart(this);
//    }

    /**
     * 비즈니스 로직
     */
    //상품 장바구니에 추가
    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    //상품 장바구니에서 삭제
    public void removeCartItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
        cartItem.dropItem();
        if(cartItem.getCart() == this) cartItem.setCart(null);
    }

    //상품 모두삭제 -> 주문 완료시
    public void removeCartItems(List<CartItem> cartItems){
        for (CartItem cartItem : cartItems) {
            this.removeCartItem(cartItem);
        }
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
                .id(id)
                .cartItemInfos(cartItemInfos)
                .deliveryPrice(getDeliveryPrice())
                .totalPrice(getTotalPrice())
                .build();
    }

}
