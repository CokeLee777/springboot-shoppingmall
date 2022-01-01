package com.shoppingmall.domain.cart;

import com.shoppingmall.domain.cartitem.CartItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Builder
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
    public void addItem(CartItem cartItem){
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    //장바구니 전체 가격 조회
    public Integer getTotalPrice(){
        Integer totalPrice = 0;
        for(CartItem cartItem: cartItems){
            totalPrice += cartItem.getTotalPrice();
        }
        return totalPrice;
    }

}
