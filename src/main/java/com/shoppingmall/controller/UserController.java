package com.shoppingmall.controller;

import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입 form
    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute("user") UserRequestDto userRequestDto){

        return "user/signUpForm";
    }

    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute UserRequestDto userRequestDto, BindingResult bindingResult){
        try{
            userService.duplicateIdentifierCheck(userRequestDto);
        } catch (Exception e){
            if(e.getClass() == DuplicatedUserException.class){
                bindingResult.reject("duplicateUser", "이미 존재하는 아이디입니다.");
            }
        }
        //검증 실패시 입력 폼으로 돌아간다.
        if(bindingResult.hasErrors()){
            log.error("errors={}", bindingResult);
            return "user/signUpForm";
        }

        userService.userRegistration(userRequestDto);

        return "/home";
    }
}
