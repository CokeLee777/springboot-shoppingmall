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

    private final UserRepository userRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;

    @PostConstruct
    public void init(){
        User admin = User.builder().identifier("admin123").password("admin123!").username("admin").email("admin@naver.com")
                .address(new Address("test", "test")).cart(new Cart()).build();
        admin.toAdminUser();
        User user = User.builder().identifier("test123").password("test123!").username("test").email("test@naver.com")
                .address(new Address("test", "test")).cart(new Cart()).build();

        ItemCategory itemCategory = ItemCategory.builder().name("노트북").build();
        Item item = Item.builder().itemCategory(itemCategory).name("맥북").price(10000).stockQuantity(5).itemImg(null).build();

        userRepository.save(admin);
        userRepository.save(user);
        itemCategoryRepository.save(itemCategory);
        itemRepository.save(item);




    }
}
