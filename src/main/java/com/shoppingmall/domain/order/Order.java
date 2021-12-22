package com.shoppingmall.domain.order;

import com.shoppingmall.domain.orderitem.OrderItem;
import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.domain.user.User;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

@Entity(name = "orders")
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column
    private String orderNumber;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

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
}
