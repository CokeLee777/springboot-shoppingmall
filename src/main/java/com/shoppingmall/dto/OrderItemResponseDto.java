package com.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.shoppingmall.dto.ItemResponseDto.ItemInfo;

public class OrderItemResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderItemInfo{
        private Integer orderPrice;
        private Integer count;
        private ItemInfo itemInfo;
    }
}
