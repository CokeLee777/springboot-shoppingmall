package com.shoppingmall.service.user;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemRequestDto;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.exception.NotExistItemException;
import com.shoppingmall.repository.ItemCategoryRepository;
import com.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    //모든 상품 조회
    public List<Item> findAll(Pageable pageable){
        return itemRepository.findAll(pageable).getContent();
    }

    //특정 카테고리의 모든 상품 조회
    public List<Item> findAllByItemCategory(ItemCategory itemCategory, Pageable pageable){
        return itemRepository.findAllByItemCategory(itemCategory, pageable).getContent();
    }

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, ItemRequestDto itemRequestDto){
        Item findItem = getItem(itemId);
        findItem.updateItem(itemRequestDto);
    }

    @Transactional
    public void deleteItem(Long itemId){
        Item findItem = getItem(itemId);
        itemRepository.delete(findItem);
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));
    }
}
