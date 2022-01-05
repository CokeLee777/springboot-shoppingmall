package com.shoppingmall.domain;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
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
