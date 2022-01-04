package com.shoppingmall.domain.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    @Column(name = "road_address")
    private String road;

    @Column(name = "detail_address")
    private String detail;
}
