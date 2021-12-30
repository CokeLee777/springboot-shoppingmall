package com.shoppingmall.service.user;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemRequestDto;
import com.shoppingmall.exception.NotExistItemException;
import com.shoppingmall.repository.ItemCategoryRepository;
import com.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item searchItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));
    }

    //모든 상품 조회
    public Page<Item> searchItems(Pageable pageable){
        return itemRepository.findAll(pageable);
    }

    public List<Item> searchItems(){
        return itemRepository.findAll();
    }

    //특정 카테고리의 모든 상품 조회
    public Page<Item> searchSameCategoryItems(ItemCategory itemCategory, Pageable pageable){
        return itemRepository.findAllByItemCategory(itemCategory, pageable);
    }

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, ItemRequestDto itemRequestDto){
        Item findItem = searchItem(itemId);
        findItem.updateItem(itemRequestDto);
    }

    @Transactional
    public void deleteItem(Long itemId){
        Item findItem = searchItem(itemId);
        itemRepository.delete(findItem);
    }
}
