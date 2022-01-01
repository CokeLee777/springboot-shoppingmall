package com.shoppingmall;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.domain.user.Address;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.repository.ItemCategoryRepository;
import com.shoppingmall.repository.ItemRepository;
import com.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.shoppingmall.dto.ItemCategoryRequestDto.*;
import static com.shoppingmall.dto.ItemRequestDto.*;

/**
 * 테스트 DB
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init(){
        User admin = User.builder().identifier("admin123").password("admin123!").username("admin").email("admin@naver.com")
                .address(new Address("test", "test")).cart(new Cart()).build();
        admin.toAdminUser();
        userRepository.save(admin);

        for(int i = 1; i < 20; i++){
            ItemCategoryCreateForm itemCategoryCreateForm = new ItemCategoryCreateForm("카테고리" + i);
            ItemCategory itemCategory = itemCategoryCreateForm.toEntity();

            ItemCreateForm itemCreateForm = new ItemCreateForm("상품" + i, 1000 - i, i, null);
            Item item = itemCreateForm.toEntity();
            item.setItemCategory(itemCategory);
            itemRepository.save(item);
        }
    }
}
