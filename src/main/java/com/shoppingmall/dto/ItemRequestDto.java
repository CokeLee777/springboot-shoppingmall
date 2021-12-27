package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter @Setter
@AllArgsConstructor
public class ItemRequestDto {

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
