package com.shoppingmall.controller;

import com.shoppingmall.domain.item.ItemCategory;
import com.shoppingmall.dto.ItemCategoryResponseDto;
import com.shoppingmall.service.user.ItemCategoryService;
import com.shoppingmall.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@Login LoginRequestDto loginRequestDto, Model model){
        log.info("home access");

        //세션에 회원 데이터가 없으면 일반 home으로 이동
        if(loginRequestDto == null){
            return "home";
        }

        model.addAttribute("user", loginRequestDto);

        return "loginHome";
    }
}
