package com.shoppingmall.repository;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 1. 모든 상품 조회
 * 2. 카테고리별로 조회
 * 공통1. 조회한 후 (1)상품등록 가격 순,(2)상품등록이 3일안으로 나온 새로운 상품순으로 정렬
 * 공통2. 조회한 후 상품 10개, 20개, 30개씩 보여주기
 */
@Transactional
@SpringBootTest
class ItemRepositoryTest {

    @Autowired EntityManager em;
    @Autowired ItemRepository itemRepository;

    @Test
    @DisplayName("모든 상품 조회 - 페이징")
    public void findAll () throws Exception
    {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<Item> page = itemRepository.findAll(pageRequest);

        //then
        List<Item> content = page.getContent();

        assertThat(content.size()).isEqualTo(10);
        assertThat(page.getTotalElements()).isEqualTo(40);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(4);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    @DisplayName("모든 상품 조회 - 페이징, 정렬(가격)")
    public void findAllSortedByPrice () throws Exception
    {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "price"));
        //when
        Page<Item> page = itemRepository.findAll(pageRequest);
        //then
        List<Item> content = page.getContent();

        assertThat(content.get(0).getPrice()).isEqualTo(1019);
        assertThat(content.get(content.size()-1).getPrice()).isEqualTo(1010);
    }

    @Test
    @DisplayName("특정 카테고리의 모든 상품 조회 - 페이징")
    public void findAllByItemCategory () throws Exception
    {
        //given
        ItemCategory itemCategory = new ItemCategory();
        em.persist(itemCategory);
        for(int i = 0; i < 10; i++){
            Item item = new Item();
//            item.setItemCategory(itemCategory);
            itemRepository.save(item);
        }
        PageRequest pageRequest = PageRequest.of(0, 10);
        //when
        Page<Item> page = itemRepository.findItemsJoinCategoryByCategory(itemCategory.getId(), pageRequest);
        //then
        List<Item> content = page.getContent();

        assertThat(content.size()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.hasNext()).isFalse();
        assertThat(page.isFirst()).isTrue();
    }

    @Test
    @DisplayName("특정 카테고리의 모든 상품 조회 - 페이징(20개씩)")
    public void findAllByItemCategoryPaging20 () throws Exception
    {
        //given
        ItemCategory itemCategory = new ItemCategory();
        em.persist(itemCategory);
        for(int i = 0; i < 30; i++){
            Item item = new Item();
//            item.setItemCategory(itemCategory);
            itemRepository.save(item);
        }
        PageRequest pageRequest = PageRequest.of(1, 20);
        //when
        Page<Item> page = itemRepository.findItemsJoinCategoryByCategory(itemCategory.getId(), pageRequest);
        //then
        List<Item> content = page.getContent();

        assertThat(content.size()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.hasNext()).isFalse();
        assertThat(page.isFirst()).isFalse();
    }
}