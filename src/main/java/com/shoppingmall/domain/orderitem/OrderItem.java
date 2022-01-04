package com.shoppingmall.domain.orderitem;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.order.Order;
import com.shoppingmall.dto.ItemResponseDto;
import com.shoppingmall.dto.OrderItemResponseDto;
import lombok.*;

import javax.persistence.*;

import static com.shoppingmall.dto.ItemResponseDto.*;
import static com.shoppingmall.dto.OrderItemResponseDto.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @Column
    private Integer orderPrice;

    @Column
    private Integer count;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private OrderItem(Integer orderPrice, Integer count, Item item){
        this.orderPrice = orderPrice;
        this.count = count;
        this.item = item;
    }

    public Integer getTotalPrice(){
        return getOrderPrice() * getCount();
    }

    public OrderItemInfo toOrderItemInfo(ItemInfo itemInfo){
        return OrderItemInfo.builder()
                .orderPrice(orderPrice)
                .count(count)
                .itemInfo(itemInfo)
                .build();
    }
}
