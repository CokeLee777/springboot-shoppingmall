package com.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
public class ItemCategoryResponseDto {

    private Long id;
    private String name;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemCategoryInfo {
        private Long id;
        private String name;
    }
}
