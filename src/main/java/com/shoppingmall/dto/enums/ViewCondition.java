package com.shoppingmall.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewCondition {
    ten("10"), twenty("20"), thirty("30");

    private String condition;
}
