package com.shoppingmall.service;

import com.shoppingmall.domain.ItemCategory;
import com.shoppingmall.exception.DuplicatedCategoryException;
import com.shoppingmall.exception.NotExistCategoryException;
import com.shoppingmall.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.shoppingmall.dto.ItemCategoryRequestDto.ItemCategoryCreateForm;
import static com.shoppingmall.dto.ItemCategoryRequestDto.ItemCategoryUpdateForm;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;

    @Transactional
    public void addItemCategory(ItemCategoryCreateForm form){
        validateDuplicateCategory(form);
        itemCategoryRepository.save(form.toEntity());
    }

    public void validateDuplicateCategory(ItemCategoryCreateForm form){
        boolean duplicated = itemCategoryRepository.existsByName(form.getName());
        if(duplicated) throw new DuplicatedCategoryException("이미 존재하는 카테고리입니다.");
    }

    @Transactional
    public void updateItemCategory(Long itemCategoryId, ItemCategoryUpdateForm form){
        ItemCategory findItemCategory = searchItemCategory(itemCategoryId);
        findItemCategory.updateItemCategory(form);
    }

    @Transactional
    public void deleteItemCategory(Long itemCategoryId){
        ItemCategory findItemCategory = searchItemCategory(itemCategoryId);
        itemCategoryRepository.delete(findItemCategory);
    }

    public List<ItemCategory> searchItemCategories(){
        return itemCategoryRepository.findAll();
    }

    public ItemCategory searchItemCategory(Long itemCategoryId) {
        return itemCategoryRepository.findById(itemCategoryId).orElseThrow(
                () -> new NotExistCategoryException("존재하지 않는 카테고리 입니다."));
    }

    public ItemCategory searchItemCategory(String categoryName){
        return itemCategoryRepository.findByName(categoryName).orElseThrow(
                () -> new NotExistCategoryException("존재하지 않는 카테고리 입니다."));
    }
}
