package com.shoppingmall.repository;

import com.shoppingmall.domain.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ItemRepositoryTest {

    @Autowired EntityManager em;
    @Autowired ItemRepository itemRepository;

    @BeforeEach
    public void beforeEach(){
        for(int i = 0; i < 15; i++){
            Item item = new Item();
            item.setPrice(1000 + i);
            itemRepository.save(item);
        }
    }
}