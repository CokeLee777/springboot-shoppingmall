package com.shoppingmall.domain;

import com.shoppingmall.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.shoppingmall.dto.ItemResponseDto.ItemInfo;
import static com.shoppingmall.dto.OrderItemResponseDto.OrderItemInfo;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @Column
    private Integer orderPrice;

    @Column
    private Integer count;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
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
