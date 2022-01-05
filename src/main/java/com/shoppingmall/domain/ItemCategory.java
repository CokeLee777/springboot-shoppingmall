package com.shoppingmall.domain;

import com.shoppingmall.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.shoppingmall.dto.ItemCategoryRequestDto.ItemCategoryUpdateForm;
import static com.shoppingmall.dto.ItemCategoryResponseDto.ItemCategoryInfo;
import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor
public class ItemCategory extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "itemCategory", cascade = ALL)
    private List<Item> items = new ArrayList<>();

    @Builder
    private ItemCategory(String name){
        this.name = name;
    }

    /**
     * 비즈니스 로직
     */
    public void updateItemCategory(ItemCategoryUpdateForm itemCategoryUpdateForm){
        this.name = itemCategoryUpdateForm.getName();
    }

    public ItemCategoryInfo toItemCategoryInfo(){
        return ItemCategoryInfo.builder()
                .id(id)
                .name(name)
                .build();
    }
}
