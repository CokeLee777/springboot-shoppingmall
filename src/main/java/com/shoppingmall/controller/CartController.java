package com.shoppingmall.controller;

import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.service.user.CartService;
import com.shoppingmall.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static com.shoppingmall.dto.UserResponseDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final UserService userService;
    private final CartService cartService;

//    @PostMapping("/cart/{userIdentifier}/add")
//    public String addItemToCart(@PathVariable("userIdentifier") String identifier, Model model){
//
//    }
}
