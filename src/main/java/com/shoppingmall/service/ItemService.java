package com.shoppingmall.service;

import com.shoppingmall.domain.Item;
import com.shoppingmall.domain.ItemCategory;
import com.shoppingmall.exception.NotExistCategoryException;
import com.shoppingmall.exception.NotExistItemException;
import com.shoppingmall.file.FileStore;
import com.shoppingmall.file.UploadFile;
import com.shoppingmall.repository.ItemCategoryRepository;
import com.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.shoppingmall.dto.ItemRequestDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final FileStore fileStore;

    //모든 상품 조회 - 페이징 x
    public List<Item> searchItems(){
        return itemRepository.findAll();
    }

    //모든 상품 조회 - 페이징 o
    public Page<Item> searchItems(Pageable pageable){
        return itemRepository.findAll(pageable);
    }
    //특정 상품 카테고리와 함께 조회
    public Item searchItemWithCategory(Long itemId){
        return itemRepository.findOneWithCategoryById(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));
    }

    //특정 카테고리의 모든 상품 조회 - 페이징 o
    public Page<Item> searchSameCategoryItems(Long categoryId, Pageable pageable){
        return itemRepository.findAllWithCategoryByCategoryId(categoryId, pageable);
    }

    //키워드로 모든 상품 조회
    public List<Item> searchItemsByKeyword(String keyword){
        return itemRepository.findAllByKeyword(keyword);
    }

    //상품 추가
    @Transactional
    public void addItem(ItemCreateForm form) throws IOException {
        //S3에 이미지 업로드
        UploadFile uploadFile = fileStore.newUpload(form.getItemImg());
        //아이템 카테고리 정보를 받아온다.
        Long categoryId = form.getCategoryId();
        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId).orElseThrow(
                () -> new NotExistCategoryException("존재하지 않는 카테고리입니다."));
        itemRepository.save(form.toEntity(itemCategory, uploadFile));
    }

    @Transactional
    public void updateItem(Long itemId, ItemUpdateForm form) throws IOException {
        //DB 업데이트
        Item findItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));
        Long categoryId = form.getCategoryId();
        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId).orElseThrow(
                () -> new NotExistCategoryException("존재하지 않는 카테고리입니다."));

        MultipartFile multipartFile = form.getItemImg();
        UploadFile uploadFile = fileStore.replaceUpload(multipartFile, findItem.getItemImgName());

        findItem.updateItem(form, uploadFile, itemCategory);
    }

    //상품 삭제
    @Transactional
    public void deleteItem(Long itemId){
        Item findItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));
        //s3에서 이미지 삭제
//        fileStore.deleteS3(findItem.getItemImgName());
        //DB에서 이미지 삭제
        itemRepository.delete(findItem);
    }
}
