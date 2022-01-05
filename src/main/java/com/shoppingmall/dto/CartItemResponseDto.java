package com.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CartItemResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CartItemInfo {

        private Long id;
        private String itemImg;
        private String itemName;
        private Integer itemPrice;
        private Integer itemCount;
    }
}
