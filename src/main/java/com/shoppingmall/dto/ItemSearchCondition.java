package com.shoppingmall.dto;

import com.shoppingmall.dto.enums.SortCondition;
import com.shoppingmall.dto.enums.ViewCondition;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchCondition {
    private SortCondition sortCondition;
    private ViewCondition sizeCondition;
}
