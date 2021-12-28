package com.shoppingmall.controller;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryResponseDto;
import com.shoppingmall.dto.ItemResponseDto;
import com.shoppingmall.dto.ItemSearchCondition;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;

    @GetMapping("/shop")
    public String itemList(
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PageableDefault(size = 10, sort = "createdDate") Pageable pageable, Model model){
        //상품 전체 조회
        List<Item> items = itemService.findAll(pageable);
        List<ItemResponseDto> itemResponseDtos = items.stream().map(Item::toItemResponseDto)
                .collect(Collectors.toList());

        //전체 카테고리 조회
        List<ItemCategory> itemCategories = itemCategoryService.getItemCategories();
        List<ItemCategoryResponseDto> itemCategoryResponseDtos = itemCategories.stream()
                .map(ItemCategory::toItemCategoryResponseDto)
                .collect(Collectors.toList());

        model.addAttribute("items", itemResponseDtos);
        model.addAttribute("itemCategories", itemCategoryResponseDtos);
        model.addAttribute("pages", pageable);
        return "item/shop";
    }

    @GetMapping("/shop/{categoryId}")
    public String itemCategoryList(
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(size = 10, sort = "createdDate") Pageable pageable, Model model){
        //현재 선택된 카테고리에 따른 상품 조회
        ItemCategory itemCategory = itemCategoryService.getItemCategory(categoryId);
        List<Item> items = itemService.findAllByItemCategory(itemCategory, pageable);
        List<ItemResponseDto> itemResponseDtos = items.stream().map(Item::toItemResponseDto)
                .collect(Collectors.toList());

        //전체 카테고리 조회
        List<ItemCategory> itemCategories = itemCategoryService.getItemCategories();
        List<ItemCategoryResponseDto> itemCategoryResponseDtos = itemCategories.stream()
                .map(ItemCategory::toItemCategoryResponseDto)
                .collect(Collectors.toList());

        model.addAttribute("items", itemResponseDtos);
        model.addAttribute("itemCategories", itemCategoryResponseDtos);
        return "item/shop";
    }

}
