package com.shoppingmall;

import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.service.user.ItemService;
import com.shoppingmall.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.shoppingmall.dto.UserRequestDto.*;

/**
 * 테스트 DB
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final ItemService itemService;
    private final UserService userService;

    @PostConstruct
    public void init(){
        UserCreateForm form = new UserCreateForm("test123", "test123!", "test",
                "test@naver.com", "test", "test");
        userService.userRegistration(form);

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
