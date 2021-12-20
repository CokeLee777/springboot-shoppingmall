package com.shoppingmall.domain.cart;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @Column
    private int totalPrice;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<Item> items = new ArrayList<>();
}
