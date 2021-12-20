package com.shoppingmall.domain.order;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.DeliveryStatus;
import com.shoppingmall.domain.user.Address;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.EnumType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(value = STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "delivery")
    private Order order;
}
