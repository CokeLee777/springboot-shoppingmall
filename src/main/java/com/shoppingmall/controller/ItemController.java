package com.shoppingmall.controller;

import com.shoppingmall.domain.Item;
import com.shoppingmall.domain.ItemCategory;
import com.shoppingmall.dto.pageCondition.ItemSearchCondition;
import com.shoppingmall.service.ItemCategoryService;
import com.shoppingmall.service.ItemService;
import com.shoppingmall.web.argumentresolver.AdminLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.ItemCategoryResponseDto.ItemCategoryInfo;
import static com.shoppingmall.dto.ItemRequestDto.ItemCreateForm;
import static com.shoppingmall.dto.ItemResponseDto.ItemInfo;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;

    //전체 상품 조회
    @GetMapping("/shop")
    public String itemList(
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //상품 전체 조회 - 페이징
        Page<Item> page = itemService.searchItems(pageable);
        //전체 상품 및 전체 카테고리 DTO로 변환
        addItemsAndCategoriesAttribute(model, page);

        log.info("전체 상품 조회 access");
        return "item/itemList";
    }

    //카테고리별로 상품 조회
    @GetMapping("/shop/{categoryId}")
    public String itemCategoryList(
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //현재 선택된 카테고리에 따른 상품 조회 - 페이징
        Page<Item> page = itemService.searchSameCategoryItems(categoryId, pageable);
        //카테고리에 따른 전체 상품, 전체 카테고리, 특정 카테고리 DTO로 변환
        addItemsAndCategoriesAttribute(model, page);

        log.info("카테고리별로 상품 조회 access");
        return "item/itemCategoryList";
    }

    private void addItemsAndCategoriesAttribute(Model model, Page<Item> page) {
        List<ItemInfo> itemInfos = getItemInfos(page);
        List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();

        model.addAttribute("items", itemInfos);
        model.addAttribute("itemCategories", itemCategoryInfos);
        model.addAttribute("page", page);
    }

    private List<ItemInfo> getItemInfos(Page<Item> page) {
        List<Item> items = page.getContent();
        return items.stream().map(Item::toItemInfo)
                .collect(Collectors.toList());
    }

    private List<ItemCategoryInfo> getItemCategoryInfos() {
        //전체 카테고리 조회
        List<ItemCategory> itemCategories = itemCategoryService.searchItemCategories();
        return itemCategories.stream()
                .map(ItemCategory::toItemCategoryInfo)
                .collect(Collectors.toList());
    }

    //상품 상세 조회
    @GetMapping("/shop/{categoryId}/{itemId}")
    public String itemDetails(@PathVariable("itemId") Long itemId, Model model){

        //현재 선택한 아이템 정보 불러오기
        Item item = itemService.searchItemWithCategory(itemId);
        ItemCategory itemCategory = item.getItemCategory();
        //리뷰정보 불러오기

        model.addAttribute("item", item.toItemInfo());
        model.addAttribute("category", itemCategory.toItemCategoryInfo());

        log.info("상품 상세 조회 itemId={}", item.getId());
        return "item/itemDetails";
    }



    //상품 추가 form
    @AdminLogin
    @GetMapping("/item/add")
    public String addItemForm(@ModelAttribute("item") ItemCreateForm itemCreateForm, Model model) throws  Exception{
        //카테고리 모두 불러오기
        List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();
        //카테고리 추가
        model.addAttribute("itemCategories", itemCategoryInfos);

        log.info("상품 추가 form access");
        return "item/itemAddForm";
    }

    //상품 추가
    @AdminLogin
    @PostMapping("/item/add")
    public String addItem(
            @Validated @ModelAttribute("item") ItemCreateForm itemCreateForm,
            BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            log.error("error={}", bindingResult);
            //카테고리 모두 불러오기
            List<ItemCategory> itemCategories = itemCategoryService.searchItemCategories();
            List<ItemCategoryInfo> itemCategoryInfos = itemCategories.stream()
                    .map(ItemCategory::toItemCategoryInfo)
                    .collect(Collectors.toList());
            model.addAttribute("itemCategories", itemCategoryInfos);
            return "item/itemAddForm";
        }

        log.info("상품 추가 완료 name={}", itemCreateForm.getName());
        itemService.addItem(itemCreateForm);
        return "redirect:/";
    }

    //키워드로 상품 검색
    @GetMapping("/search")
    public String keywordItemList(@RequestParam("search") String keyword, Model model){
        List<Item> items = itemService.searchItemsByKeyword(keyword);
        List<ItemInfo> itemInfos = items.stream()
                .map(Item::toItemInfo)
                .collect(Collectors.toList());

        model.addAttribute("items", itemInfos);

        return "item/itemSearchList";
    }
}
