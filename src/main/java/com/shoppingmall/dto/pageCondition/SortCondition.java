package com.shoppingmall.dto.pageCondition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortCondition {
    createdDate("등록일"), price("가격");

    private String sort;
}
