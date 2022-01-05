package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.domain.Item;
import com.shoppingmall.domain.ItemCategory;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ItemRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemCreateForm {
        @NotBlank(message = "상품명을 작성해주세요.")
        private String name;

        @NotNull(message = "상품 가격을 작성해주세요.")
        private Integer price;

        @NotNull(message = "상품 수량을 작성해주세요.")
        private Integer stockQuantity;

        @NotNull(message = "상품 카테고리를 선택해주세요.")
        private Long categoryId;

        private String itemImg;

        public Item toEntity(ItemCategory itemCategory){
            return Item.builder()
                    .name(name)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .itemImg(itemImg)
                    .itemCategory(itemCategory)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemUpdateForm {
        @NotBlank(message = "상품명을 작성해주세요.")
        private String name;

        @NotNull(message = "상품 가격을 작성해주세요.")
        private Integer price;

        @NotNull(message = "상품 수량을 작성해주세요.")
        private Integer stockQuantity;

        @NotNull(message = "상품 상태를 선택해주세요.")
        private ItemStatus itemStatus;

        @NotNull(message = "상품 카테고리를 선택해주세요.")
        private Long categoryId;

        private String itemImg;
    }
}
