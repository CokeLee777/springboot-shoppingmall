package com.shoppingmall.controller;

import com.shoppingmall.domain.enums.UserRole;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.exception.DuplicatedCategoryException;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.service.user.UserService;
import com.shoppingmall.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.shoppingmall.dto.ItemCategoryRequestDto.*;
import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemCategoryController {

    private final UserService userService;
    private final ItemCategoryService itemCategoryService;

    private void addSessionAttribute(LoginUserForm loginUserForm, Model model) {
        model.addAttribute("user", loginUserForm);
    }

    @GetMapping("/category/add")
    public String categoryAddForm(
            @Login LoginUserForm loginUserForm,
            @ModelAttribute("category") ItemCategoryCreateForm itemCategoryCreateForm,
            HttpServletRequest request, HttpServletResponse response, Model model) throws Exception{
        //관리자인지 아닌지 판별
        String requestURI = request.getRequestURI();
        UserResponseDto.UserProfileInfo userProfileInfo = userService.searchProfiles(loginUserForm.getIdentifier());

        if(userProfileInfo.getRole() == UserRole.USER){
            response.sendRedirect("/sign-in?redirectURL=" + requestURI);
        }

        log.info("상품카테고리 추가 form access");
        addSessionAttribute(loginUserForm, model);
        return "category/categoryAddForm";
    }

    @PostMapping("/category/add")
    public String categoryAdd(
            @Login LoginUserForm loginUserForm,
            @Validated @ModelAttribute("category") ItemCategoryCreateForm itemCategoryCreateForm,
            BindingResult bindingResult, Model model) throws Exception{

        if(bindingResult.hasErrors()){
            log.error("error={}", bindingResult);
            return "category/categoryAddForm";
        }

        try{
            itemCategoryService.addItemCategory(itemCategoryCreateForm);
        } catch (Exception e){
            log.error("error={}", e.getMessage());
            bindingResult.reject("duplicatedCategoryException", new Object[]{DuplicatedCategoryException.class}, null);
            return "category/categoryAddForm";
        }

        log.info("상품카테고리 추가 name={}", itemCategoryCreateForm.getName());
        addSessionAttribute(loginUserForm, model);

        return "redirect:/shop";
    }
}
