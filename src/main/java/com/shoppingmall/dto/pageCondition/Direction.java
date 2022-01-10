package com.shoppingmall.dto.pageCondition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Direction {
    asc("오름차순"), desc("내림차순");

    private String direction;
}
