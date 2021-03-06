package com.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ItemCategoryResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemCategoryInfo {
        private Long id;
        private String name;
    }
}
