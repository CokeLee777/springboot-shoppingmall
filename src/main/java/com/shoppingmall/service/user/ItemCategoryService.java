package com.shoppingmall.service.user;

import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryRequestDto;
import com.shoppingmall.exception.NotExistCategoryException;
import com.shoppingmall.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;

    @Transactional
    public void saveItemCategory(ItemCategory itemCategory){
        itemCategoryRepository.save(itemCategory);
    }

    @Transactional
    public void updateItemCategory(Long itemCategoryId, ItemCategoryRequestDto itemCategoryRequestDto){
        ItemCategory findItemCategory = getItemCategory(itemCategoryId);
        findItemCategory.updateItemCategory(itemCategoryRequestDto);
    }

    @Transactional
    public void deleteItemCategory(Long itemCategoryId){
        ItemCategory findItemCategory = getItemCategory(itemCategoryId);
        itemCategoryRepository.delete(findItemCategory);
    }

    public ItemCategory getItemCategory(Long itemCategoryId) {
        return itemCategoryRepository.findById(itemCategoryId).orElseThrow(
                () -> new NotExistCategoryException("존재하지 않는 카테고리 입니다."));
    }

    public ItemCategory getItemCategory(String categoryName){
        return itemCategoryRepository.findByName(categoryName).orElseThrow(
                () -> new NotExistCategoryException("존재하지 않는 카테고리 입니다."));
    }

    public List<ItemCategory> getItemCategories(){
        return itemCategoryRepository.findAll();
    }
}
