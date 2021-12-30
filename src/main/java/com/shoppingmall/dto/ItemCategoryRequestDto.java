package com.shoppingmall.dto;

import com.shoppingmall.domain.item.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Builder
@Getter @Setter
@AllArgsConstructor
public class ItemCategoryRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateItemCategoryForm {

        @NotBlank(message = "카테고리명을 작성해주세요.")
        private String name;

        public ItemCategory toEntity(){
            return ItemCategory.builder()
                    .name(name)
                    .build();
        }
    }

    @NotBlank(message = "카테고리명을 작성해주세요.")
    private String name;

    public ItemCategory toEntity(){
        return ItemCategory.builder()
                .name(name)
                .build();
    }
}
