package com.shoppingmall.domain.orderitem;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @Column
    private int orderPrice;

    @Column
    private int count;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_id")
    private Item item;
}
