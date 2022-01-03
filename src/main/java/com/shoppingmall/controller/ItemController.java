package com.shoppingmall.controller;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.pageCondition.ItemSearchCondition;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.ItemService;
import com.shoppingmall.service.user.UserService;
import com.shoppingmall.web.argumentresolver.Login;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.ItemCategoryResponseDto.*;
import static com.shoppingmall.dto.ItemRequestDto.*;
import static com.shoppingmall.dto.ItemResponseDto.*;
import static com.shoppingmall.dto.UserRequestDto.*;
import static com.shoppingmall.dto.UserResponseDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;
    private final UserService userService;

    //전체 상품 조회
    @GetMapping("/shop")
    public String itemList(
            @Login LoginUserForm loginUserForm,
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //상품 전체 조회 - 페이징
        Page<Item> page = itemService.searchItems(pageable);
        //전체 상품 및 전체 카테고리 DTO로 변환
        List<ItemInfo> itemInfos = getItemResponseDtos(page);
        List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();

        addSessionAttribute(loginUserForm, model);
        model.addAttribute("items", itemInfos);
        model.addAttribute("itemCategories", itemCategoryInfos);
        model.addAttribute("page", page);
        return "item/itemList";
    }

    private void addSessionAttribute(LoginUserForm loginUserForm, Model model) {
        model.addAttribute("user", loginUserForm);
    }

    //카테고리별로 상품 조회
    @GetMapping("/shop/{categoryId}")
    public String itemCategoryList(
            @Login LoginUserForm loginUserForm,
            @ModelAttribute("searchCondition") ItemSearchCondition itemSearchCondition,
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(page =  0, size = 10, sort = "createdDate") Pageable pageable, Model model){
        //현재 선택된 카테고리에 따른 상품 조회 - 페이징
        ItemCategory itemCategory = itemCategoryService.searchItemCategory(categoryId);
        Page<Item> page = itemService.searchSameCategoryItems(itemCategory, pageable);
        //카테고리에 따른 전체 상품, 전체 카테고리, 특정 카테고리 DTO로 변환
        List<ItemInfo> itemInfos = getItemResponseDtos(page);
        List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();
        ItemCategoryInfo itemCategoryInfo = itemCategory.toItemCategoryInfo();

        addSessionAttribute(loginUserForm, model);
        model.addAttribute("itemCategory", itemCategoryInfo);
        model.addAttribute("items", itemInfos);
        model.addAttribute("itemCategories", itemCategoryInfos);
        model.addAttribute("page", page);
        return "item/itemCategoryList";
    }

    private List<ItemInfo> getItemResponseDtos(Page<Item> page) {
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
    public String itemDetails(
            @Login LoginUserForm loginUserForm,
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            Model model){

        //현재 선택한 아이템 정보 불러오기
        Item item = itemService.searchItem(itemId);
        ItemCategory itemCategory = itemCategoryService.searchItemCategory(categoryId);

        ItemInfo itemInfo = item.toItemInfo();
        ItemCategoryInfo itemCategoryInfo = itemCategory.toItemCategoryInfo();

        addSessionAttribute(loginUserForm, model);
        model.addAttribute("item", itemInfo);
        model.addAttribute("category", itemCategoryInfo);
        return "item/itemDetails";
    }

    //상품 추가
    @GetMapping("/item/add")
    public String addItemForm(
            @Login LoginUserForm loginUserForm,
            @ModelAttribute("item") ItemCreateForm itemCreateForm,
            HttpServletRequest request, HttpServletResponse response, Model model) throws  Exception{
        //관리자인지 아닌지 판별
        String requestURI = request.getRequestURI();
        UserProfileInfo userProfileInfo = userService.searchProfiles(loginUserForm.getIdentifier());

        if(userProfileInfo.getRole() == UserRole.USER){
            response.sendRedirect("/sign-in?redirectURL=" + requestURI);
        }
        //카테고리 모두 불러오기
        List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();

        log.info("상품 추가 form access");
        addSessionAttribute(loginUserForm, model);
        model.addAttribute("itemCategories", itemCategoryInfos);
        return "item/itemAddForm";
    }

    //상품 추가
    @PostMapping("/item/add")
    public String addItem(
            @Login LoginUserForm loginUserForm,
            @Validated @ModelAttribute("item") ItemCreateForm itemCreateForm,
            BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            log.error("error={}", bindingResult);
            //카테고리 모두 불러오기
            List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();
            model.addAttribute("itemCategories", itemCategoryInfos);
            return "item/itemAddForm";
        }

        addSessionAttribute(loginUserForm, model);

        log.info("상품 추가 완료 name={}", itemCreateForm.getName());
        itemService.addItem(itemCreateForm);
        return "redirect:/";
    }

}
