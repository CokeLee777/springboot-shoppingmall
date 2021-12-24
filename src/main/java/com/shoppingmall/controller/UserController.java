package com.shoppingmall.controller;

import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입 form
    @GetMapping("/user/new")
    public String registrationForm(Model model){
        model.addAttribute("createForm", new UserRequestDto());

        return "user/registrationForm";
    }
}
