package com.shoppingmall.controller;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryResponseDto;
import com.shoppingmall.dto.ItemResponseDto;
import com.shoppingmall.dto.pageCondition.SortCondition;
import com.shoppingmall.dto.pageCondition.ViewCondition;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;

    private List<SortCondition> getSortRequest(){
        List<SortCondition> sortConditions = new ArrayList<>();
        sortConditions.add(new SortCondition("createdDate", "desc"));
        sortConditions.add(new SortCondition("createdDate", "asc"));
        sortConditions.add(new SortCondition("price", "desc"));
        sortConditions.add(new SortCondition("price", "desc"));
        return sortConditions;
    }

    private List<ViewCondition> getSizeRequest(){
        List<ViewCondition> viewConditions = new ArrayList<>();
        viewConditions.add(new ViewCondition("10"));
        viewConditions.add(new ViewCondition("20"));
        viewConditions.add(new ViewCondition("30"));
        return viewConditions;
    }

    //전체 상품 조회
    @GetMapping("/shop")
    public String itemList(@PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //상품 전체 조회
        Page<Item> page = itemService.findAll(pageable);
        List<Item> items = page.getContent();
        List<ItemResponseDto> itemResponseDtos = items.stream().map(Item::toItemResponseDto)
                .collect(Collectors.toList());

        List<ItemCategoryResponseDto> itemCategoryResponseDtos = getItemCategoryResponseDtos();

        model.addAttribute("sortCondition", getSortRequest());
        model.addAttribute("sizeCondition", getSizeRequest());
        model.addAttribute("items", itemResponseDtos);
        model.addAttribute("itemCategories", itemCategoryResponseDtos);
        model.addAttribute("page", page);
        return "item/itemList";
    }

    //카테고리별로 상품 조회
    @GetMapping("/shop/{categoryId}")
    public String itemCategoryList(
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //현재 선택된 카테고리에 따른 상품 조회
        ItemCategory itemCategory = itemCategoryService.getItemCategory(categoryId);
        Page<Item> page = itemService.findAllByItemCategory(itemCategory, pageable);
        List<Item> items = page.getContent();
        List<ItemResponseDto> itemResponseDtos = items.stream().map(Item::toItemResponseDto)
                .collect(Collectors.toList());

        //전체 카테고리 조회
        List<ItemCategoryResponseDto> itemCategoryResponseDtos = getItemCategoryResponseDtos();

        model.addAttribute("items", itemResponseDtos);
        model.addAttribute("itemCategories", itemCategoryResponseDtos);
        model.addAttribute("page", page);
        return "item/itemList";
    }

    //상품 상세 조회
    @GetMapping("/shop/{categoryId}/{itemId}")
    public String itemDetails(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            Model model){
        Item item = itemService.findOne(itemId);
        model.addAttribute("item", item.toItemResponseDto());
        return "item/itemDetails";
    }

    private List<ItemCategoryResponseDto> getItemCategoryResponseDtos() {
        //전체 카테고리 조회
        List<ItemCategory> itemCategories = itemCategoryService.getItemCategories();
        return itemCategories.stream()
                .map(ItemCategory::toItemCategoryResponseDto)
                .collect(Collectors.toList());
    }

}
