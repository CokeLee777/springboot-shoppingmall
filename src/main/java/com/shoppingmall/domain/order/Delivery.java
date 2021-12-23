package com.shoppingmall.domain.order;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.domain.enums.DeliveryStatus;
import com.shoppingmall.domain.user.Address;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.*;

@Entity
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class Delivery extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(value = STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "delivery", cascade = ALL)
    private Order order;
}
