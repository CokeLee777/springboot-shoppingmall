package com.shoppingmall.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {
    SALE("판매중"), TEMPSOLDOUT("일시품절"), SOLDOUT("품절");

    private String status;
}
