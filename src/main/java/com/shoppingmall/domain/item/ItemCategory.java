package com.shoppingmall.domain.item;

import com.shoppingmall.domain.common.BaseEntity;
import com.shoppingmall.dto.ItemCategoryRequestDto;
import com.shoppingmall.dto.ItemCategoryResponseDto;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategory extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "itemCategory", cascade = ALL)
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    /**
     * 비즈니스 로직
     */
    public void updateItemCategory(ItemCategoryRequestDto itemCategoryRequestDto){
        this.name = itemCategoryRequestDto.getName();
    }

    public ItemCategoryResponseDto toItemCategoryResponseDto(){
        return ItemCategoryResponseDto.builder()
                .id(id)
                .name(name)
                .build();
    }
}
