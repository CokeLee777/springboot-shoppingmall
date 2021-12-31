package com.shoppingmall;

import com.shoppingmall.domain.enums.ItemStatus;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryRequestDto;
import com.shoppingmall.dto.ItemRequestDto;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.ItemService;
import com.shoppingmall.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.shoppingmall.dto.ItemCategoryRequestDto.*;
import static com.shoppingmall.dto.ItemRequestDto.*;
import static com.shoppingmall.dto.UserRequestDto.*;

/**
 * 테스트 DB
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;
    private final UserService userService;

    @PostConstruct
    public void init(){
        UserCreateForm userCreateForm = new UserCreateForm("test123", "test123!", "test",
                "test@naver.com", "test", "test");
        userService.userRegistration(userCreateForm);

        for(int i = 0; i < 20; i++){
            ItemCategoryCreateForm itemCategoryCreateForm = new ItemCategoryCreateForm("카테고리" + i);
            ItemCreateForm itemCreateForm = new ItemCreateForm("상품" + i, 1000 - i, i, ItemStatus.SALE, null);
            Item item = itemCreateForm.toEntity();
            item.setItemCategory(itemCategoryCreateForm.toEntity());
            itemService.saveItem(item);
        }
    }
}
