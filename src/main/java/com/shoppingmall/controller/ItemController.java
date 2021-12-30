package com.shoppingmall.controller;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryResponseDto;
import com.shoppingmall.dto.ItemResponseDto;
import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.dto.pageCondition.ItemSearchCondition;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.ItemService;
import com.shoppingmall.web.argumentresolver.Login;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;

    //전체 상품 조회
    @GetMapping("/shop")
    public String itemList(
            @Login UserRequestDto.LoginRequestDto loginRequestDto,
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //상품 전체 조회 - 페이징
        Page<Item> page = itemService.findAll(pageable);
        //전체 상품 및 전체 카테고리 DTO로 변환
        List<ItemResponseDto> itemResponseDtos = getItemResponseDtos(page);
        List<ItemCategoryResponseDto> itemCategoryResponseDtos = getItemCategoryResponseDtos();

        addSessionAttribute(loginRequestDto, model);
        model.addAttribute("items", itemResponseDtos);
        model.addAttribute("itemCategories", itemCategoryResponseDtos);
        model.addAttribute("page", page);
        return "item/itemList";
    }

    private void addSessionAttribute(UserRequestDto.LoginRequestDto loginRequestDto, Model model) {
        model.addAttribute("user", loginRequestDto);
    }

    //카테고리별로 상품 조회
    @GetMapping("/shop/{categoryId}")
    public String itemCategoryList(
            @Login UserRequestDto.LoginRequestDto loginRequestDto,
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //현재 선택된 카테고리에 따른 상품 조회 - 페이징
        ItemCategory itemCategory = itemCategoryService.getItemCategory(categoryId);
        Page<Item> page = itemService.findAllByItemCategory(itemCategory, pageable);
        //카테고리에 따른 전체 상품, 전체 카테고리, 특정 카테고리 DTO로 변환
        List<ItemResponseDto> itemResponseDtos = getItemResponseDtos(page);
        List<ItemCategoryResponseDto> itemCategoryResponseDtos = getItemCategoryResponseDtos();
        ItemCategoryResponseDto itemCategoryResponseDto = itemCategory.toItemCategoryResponseDto();

        addSessionAttribute(loginRequestDto, model);
        model.addAttribute("itemCategory", itemCategoryResponseDto);
        model.addAttribute("items", itemResponseDtos);
        model.addAttribute("itemCategories", itemCategoryResponseDtos);
        model.addAttribute("page", page);
        return "item/itemCategoryList";
    }

    private List<ItemResponseDto> getItemResponseDtos(Page<Item> page) {
        List<Item> items = page.getContent();
        return items.stream().map(Item::toItemResponseDto)
                .collect(Collectors.toList());
    }

    private List<ItemCategoryResponseDto> getItemCategoryResponseDtos() {
        //전체 카테고리 조회
        List<ItemCategory> itemCategories = itemCategoryService.getItemCategories();
        return itemCategories.stream()
                .map(ItemCategory::toItemCategoryResponseDto)
                .collect(Collectors.toList());
    }

    //상품 상세 조회
    @GetMapping("/shop/{categoryId}/{itemId}")
    public String itemDetails(
            @Login UserRequestDto.LoginRequestDto loginRequestDto,
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            Model model){
        Item item = itemService.findOne(itemId);
        ItemCategory itemCategory = itemCategoryService.getItemCategory(categoryId);
        ItemResponseDto itemResponseDto = item.toItemResponseDto();
        ItemCategoryResponseDto itemCategoryResponseDto = itemCategory.toItemCategoryResponseDto();

        addSessionAttribute(loginRequestDto, model);
        model.addAttribute("item", itemResponseDto);
        model.addAttribute("category", itemCategoryResponseDto);
        return "item/itemDetails";
    }



}
