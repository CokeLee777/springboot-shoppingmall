package com.shoppingmall.service;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.exception.NotExistCategoryException;
import com.shoppingmall.exception.NotExistItemException;
import com.shoppingmall.repository.ItemCategoryRepository;
import com.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.shoppingmall.dto.ItemRequestDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    public Item searchItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));
    }

    public Item searchItemWithCategory(Long itemId){
        return itemRepository.findItemJoinCategory(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));
    }

    //모든 상품 조회 - 페이징 o
    public Page<Item> searchItems(Pageable pageable){
        return itemRepository.findAll(pageable);
    }
    //모든 상품 조회 - 페이징 x
    public List<Item> searchItems(){
        return itemRepository.findAll();
    }
    //키워드로 모든 상품 조회
    public List<Item> searchItemsByKeyword(String keyword){
        return itemRepository.findAllByKeyword(keyword);
    }

    //특정 카테고리의 모든 상품 조회
    public Page<Item> searchSameCategoryItems(Long categoryId, Pageable pageable){
        return itemRepository.findItemsJoinCategoryByCategory(categoryId, pageable);
    }

    @Transactional
    public void addItem(ItemCreateForm form){
        //아이템 카테고리 정보를 받아온다.
        Long categoryId = form.getCategoryId();
        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId).orElseThrow(
                () -> new NotExistCategoryException("존재하지 않는 카테고리입니다."));
        itemRepository.save(form.toEntity(itemCategory));
    }

    @Transactional
    public void updateItem(Long itemId, ItemUpdateForm itemUpdateForm){
        Item findItem = searchItem(itemId);
        findItem.updateItem(itemUpdateForm);
    }

    @Transactional
    public void deleteItem(Long itemId){
        Item findItem = searchItem(itemId);
        itemRepository.delete(findItem);
    }
}
