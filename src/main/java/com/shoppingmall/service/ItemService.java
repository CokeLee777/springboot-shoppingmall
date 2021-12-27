package com.shoppingmall.service;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 전체 상품 조회: 10개씩 페이지를 나눈다.
     * 1. 상품 등록 날짜순으로 조회
     * 2. 상품 카테고리로 조회
     */
//    public UserResponseDto searchProductList(){
//
//    }
}
