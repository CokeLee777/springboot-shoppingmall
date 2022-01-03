package com.shoppingmall.dto;

import com.shoppingmall.domain.cartitem.CartItem;
import com.shoppingmall.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class CartItemRequestDto {

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
