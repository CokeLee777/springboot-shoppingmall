package com.shoppingmall.domain.order;

import com.shoppingmall.domain.orderitem.OrderItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.dto.OrderItemResponseDto;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.OrderItemResponseDto.*;
import static com.shoppingmall.dto.OrderResponseDto.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

@Entity(name = "orders")
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column
    private String orderNumber;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    private Order(String orderNumber, OrderStatus orderStatus, User user, Delivery delivery, List<OrderItem> orderItems){
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        setUser(user);
        setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            addOrderItem(orderItem);
        }
    }

    /**
     * 연관관계 메서드
     */
    private void setUser(User user){
        if(this.user != null) {
            this.user.getOrders().remove(this);
        }
        this.user = user;
        user.getOrders().add(this);
    }

    private void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    private void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //비즈니스 로직
    public Integer getTotalPrice(){
        Integer totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        if(totalPrice < 30000) totalPrice += 2500;

        return totalPrice;
    }

    public OrderInfo toOrderInfo(List<OrderItemInfo> orderItemInfos){
        return OrderInfo.builder()
                .orderNumber(orderNumber)
                .orderStatus(orderStatus)
                .deliveryStatus(delivery.getDeliveryStatus())
                .address(delivery.getAddress())
                .orderItemInfos(orderItemInfos)
                .totalPrice(getTotalPrice())
                .deliveryPrice(getTotalPrice() < 30000 ? 2500 : 0)
                .build();
    }
}
