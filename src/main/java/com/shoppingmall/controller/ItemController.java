package com.shoppingmall.controller;

import com.shoppingmall.domain.Item;
import com.shoppingmall.domain.ItemCategory;
import com.shoppingmall.dto.pageCondition.ItemSearchCondition;
import com.shoppingmall.file.FileStore;
import com.shoppingmall.service.ItemCategoryService;
import com.shoppingmall.service.ItemService;
import com.shoppingmall.web.argumentresolver.AdminLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.ItemCategoryResponseDto.ItemCategoryInfo;
import static com.shoppingmall.dto.ItemRequestDto.ItemCreateForm;
import static com.shoppingmall.dto.ItemRequestDto.ItemUpdateForm;
import static com.shoppingmall.dto.ItemResponseDto.ItemInfo;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;
    private final FileStore fileStore;

    //์ํ ์กฐํ
    @GetMapping(value = {"/shop/{categoryId}", "/shop"})
    public String itemCategoryList(
            @PathVariable(value = "categoryId", required = false) Optional<Long> categoryId,
            @ModelAttribute("searchCondition") ItemSearchCondition condition,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction, Model model){
        //ํ์ด์ง ์กฐ๊ฑด
        PageRequest pageRequest = PageRequest.of(pageNum, 10, Sort.by(Sort.Direction.fromString(direction), sort));
        if(categoryId.isPresent()){
            //ํ์ฌ ์?ํ๋ ์นดํ๊ณ?๋ฆฌ์ ๋ฐ๋ฅธ ์ํ ์กฐํ - ํ์ด์ง
            Page<Item> page = itemService.searchSameCategoryItems(categoryId.get(), pageRequest);
            //์นดํ๊ณ?๋ฆฌ์ ๋ฐ๋ฅธ ์?์ฒด ์ํ, ์?์ฒด ์นดํ๊ณ?๋ฆฌ, ํน์? ์นดํ๊ณ?๋ฆฌ DTO๋ก ๋ณํ
            addItemsAndCategoriesAttribute(model, page);
        } else {
            //์ํ ์?์ฒด ์กฐํ - ํ์ด์ง
            Page<Item> page = itemService.searchItems(pageRequest);
            //์?์ฒด ์ํ ๋ฐ ์?์ฒด ์นดํ๊ณ?๋ฆฌ DTO๋ก ๋ณํ
            addItemsAndCategoriesAttribute(model, page);
        }
        log.info("์นดํ๊ณ?๋ฆฌ๋ณ๋ก ์ํ ์กฐํ access");
        return "item/itemList";
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
        //์?์ฒด ์นดํ๊ณ?๋ฆฌ ์กฐํ
        List<ItemCategory> itemCategories = itemCategoryService.searchItemCategories();
        return itemCategories.stream()
                .map(ItemCategory::toItemCategoryInfo)
                .collect(Collectors.toList());
    }

    //์ํ ์์ธ ์กฐํ
    @GetMapping("/shop/{categoryId}/{itemId}")
    public String itemDetails(@PathVariable("itemId") Long itemId, Model model){

        //ํ์ฌ ์?ํํ ์์ดํ ์?๋ณด ๋ถ๋ฌ์ค๊ธฐ
        Item item = itemService.searchItemWithCategory(itemId);
        ItemCategory itemCategory = item.getItemCategory();

        model.addAttribute("item", item.toItemInfo());
        model.addAttribute("category", itemCategory.toItemCategoryInfo());

        log.info("์ํ ์์ธ ์กฐํ itemId={}", item.getId());
        return "item/itemDetails";
    }

    //์ํ ์ถ๊ฐ form
    @AdminLogin
    @GetMapping("/item/add")
    public String addItemForm(@ModelAttribute("item") ItemCreateForm form, Model model){
        //์นดํ๊ณ?๋ฆฌ ๋ชจ๋ ๋ถ๋ฌ์ค๊ธฐ
        List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();
        //์นดํ๊ณ?๋ฆฌ ์ถ๊ฐ
        model.addAttribute("itemCategories", itemCategoryInfos);

        log.info("์ํ ์ถ๊ฐ form access");
        return "item/itemAddForm";
    }

    //์ํ ์ถ๊ฐ
    @AdminLogin
    @PostMapping("/item/add")
    public String addItem(
            @Validated @ModelAttribute("item") ItemCreateForm form,
            BindingResult bindingResult, Model model) throws IOException {

        if(bindingResult.hasErrors()){
            log.error("error={}", bindingResult);
            //์นดํ๊ณ?๋ฆฌ ๋ชจ๋ ๋ถ๋ฌ์ค๊ธฐ
            List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();

            model.addAttribute("itemCategories", itemCategoryInfos);
            return "item/itemAddForm";
        }

        //์ด๋ฏธ์ง ์๋ก๋ ๋ฐ DB์ ์?์ฅ
        itemService.addItem(form);

        log.info("์ํ ์ถ๊ฐ ์๋ฃ name={}", form.getName());
        return "redirect:/";
    }

    //์ด๋ฏธ์ง๋ฅผ ๋ค์ด๋ก๋ํ? ๋ API
    @ResponseBody
    @GetMapping("/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + filename);
    }

    //ํค์๋๋ก ์ํ ๊ฒ์
    @GetMapping("/search")
    public String keywordItemList(@RequestParam("search") String keyword, Model model){
        List<Item> items = itemService.searchItemsByKeyword(keyword);
        List<ItemInfo> itemInfos = items.stream()
                .map(Item::toItemInfo)
                .collect(Collectors.toList());

        model.addAttribute("items", itemInfos);

        return "item/itemSearchList";
    }

    @AdminLogin
    @GetMapping("/item/{itemId}/edit")
    public String editItemForm(@PathVariable("itemId") Long itemId, Model model){
        Item findItem = itemService.searchItemWithCategory(itemId);
        List<ItemCategoryInfo> itemCategoryInfos = getItemCategoryInfos();

        model.addAttribute("itemCategories", itemCategoryInfos);
        model.addAttribute("itemUpdateForm", findItem.toItemUpdateForm());
        return "item/itemEditForm";
    }

    @PostMapping("/item/{itemId}/edit")
    public String editItem(
            @PathVariable("itemId") Long itemId,
            @Validated @ModelAttribute("itemUpdateForm") ItemUpdateForm form,
            BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()){
            log.error("error={}", bindingResult);
            return "item/itemEditForm";
        }
        //์ํ ์์?
        itemService.updateItem(itemId, form);

        log.info("์ํ ์์? ์๋ฃ id={}", itemId);
        return "redirect:/shop";
    }

    @AdminLogin
    @GetMapping("/item/{itemId}/delete")
    public String deleteItem(@PathVariable("itemId") Long itemId){
        itemService.deleteItem(itemId);
        log.info("์ํ ์ญ์? id={}", itemId);
        return "redirect:/shop";
    }
}
