package com.shoppingmall.domain.order;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.DeliveryStatus;
import com.shoppingmall.domain.user.Address;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(value = STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Builder
    private Delivery(DeliveryStatus deliveryStatus, Address address){
        this.deliveryStatus = deliveryStatus;
        this.address = address;
    }
}
