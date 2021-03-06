package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ItemResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemInfo {
        private Long id;
        private Long itemCategoryId;
        private String itemImgUrl;
        private String name;
        private Integer price;
        private Integer stockQuantity;
        private ItemStatus itemStatus;
    }
}
