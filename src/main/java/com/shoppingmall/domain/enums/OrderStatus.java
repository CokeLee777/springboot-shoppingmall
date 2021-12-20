package com.shoppingmall.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    ORDER("주문완료"), CANCEL("주문취소");

    private String status;
}
