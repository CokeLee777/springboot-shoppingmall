package com.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.shoppingmall.dto.CartItemRequestDto.*;

public class CartRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CartInfo {

        private List<CartItemInfo> cartItemInfos;
        private Integer deliveryPrice;
        private Integer totalPrice;
    }
}
