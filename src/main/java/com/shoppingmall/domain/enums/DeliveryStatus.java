package com.shoppingmall.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
    READY("배송준비"), SHIPPING("배송중"), COMPLETE("배송완료");

    private String status;
}
