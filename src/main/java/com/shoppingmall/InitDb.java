package com.shoppingmall;

import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryRequestDto;
import com.shoppingmall.dto.ItemRequestDto;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 테스트 DB
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;

    @PostConstruct
    public void init(){

        for(int i = 0; i < 20; i++){
            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setName("카테고리" + i);
            Item item = new Item();
            item.setName("상품" + i);
            item.setPrice(1000 - i);
            if(i < 10) item.setItemStatus(ItemStatus.SALE);
            else if(i < 15) item.setItemStatus(ItemStatus.TEMPSOLDOUT);
            else item.setItemStatus(ItemStatus.SOLDOUT);
            item.setItemCategory(itemCategory);
            itemService.saveItem(item);
        }
    }
}
