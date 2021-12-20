package com.shoppingmall.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
public class Address {

    @Column(name = "road_address")
    private String road;

    @Column(name = "detail_address")
    private String detail;
}
