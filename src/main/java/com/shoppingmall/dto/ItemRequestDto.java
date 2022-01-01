package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.ItemCategoryRequestDto.*;
import static com.shoppingmall.dto.ItemCategoryResponseDto.*;

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

        @NotBlank(message = "상품 사진을 등록해주세요.")
        private String itemImg;

        public Item toEntity(){
            return Item.builder()
                    .name(name)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .itemImg(itemImg)
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

        @NotBlank(message = "상품 사진을 등록해주세요.")
        private String itemImg;

    }
}
