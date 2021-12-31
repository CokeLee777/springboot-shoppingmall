package com.shoppingmall.domain.cart;

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

    @Column
    private Integer totalPrice;

    @OneToOne(mappedBy = "cart", fetch = LAZY, cascade = ALL)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = ALL)
    private List<Item> items = new ArrayList<>();

    /**
     * 연관관계 메서드
     */

    public void addItems(Item item){
        this.items.add(item);
        item.setCart(this);
    }

}
