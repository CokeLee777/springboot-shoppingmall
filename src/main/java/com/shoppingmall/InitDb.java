package com.shoppingmall;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryRequestDto;
import com.shoppingmall.dto.ItemRequestDto;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 테스트 DB
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final ItemService itemService;
    private final ItemCategoryService adminItemCategoryService;

    @PostConstruct
    public void init(){

        //아이템 추가
        for(int i = 0; i < 20; i++){
            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setName("카테고리" + i);
            Item item = new Item();
            item.setName("상품" + i);
            item.setPrice(1000 + i);
            item.setItemCategory(itemCategory);
            itemService.saveItem(item);
        }
    }
}
