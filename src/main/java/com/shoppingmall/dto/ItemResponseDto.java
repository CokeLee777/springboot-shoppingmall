package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
public class ItemResponseDto {

    private String name;
    private Integer price;
    private Integer stockQuantity;
    private ItemStatus itemStatus;
    private String itemImg;
}
