package com.shoppingmall.controller;

import com.shoppingmall.exception.DuplicatedCategoryException;
import com.shoppingmall.service.ItemCategoryService;
import com.shoppingmall.web.argumentresolver.AdminLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static com.shoppingmall.dto.ItemCategoryRequestDto.ItemCategoryCreateForm;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @AdminLogin
    @GetMapping("/category/add")
    public String categoryAddForm(
            @ModelAttribute("category") ItemCategoryCreateForm form){
        log.info("상품카테고리 추가 form access");
        return "category/categoryAddForm";
    }

    @AdminLogin
    @PostMapping("/category/add")
    public String categoryAdd(
            @Validated @ModelAttribute("category") ItemCategoryCreateForm form,
            BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.error("error={}", bindingResult);
            return "category/categoryAddForm";
        }

        try{
            itemCategoryService.addItemCategory(form);
        } catch (Exception e){
            log.error("error={}", e.getMessage());
            bindingResult.reject("duplicatedCategoryException", new Object[]{DuplicatedCategoryException.class}, null);
            return "category/categoryAddForm";
        }

        log.info("상품카테고리 추가 name={}", form.getName());
        return "redirect:/shop";
    }

    @AdminLogin
    @GetMapping("/category/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId){
        itemCategoryService.deleteItemCategory(categoryId);

        log.info("상품 카테고리 삭제 id={}", categoryId);
        return "redirect:/shop";
    }
}
