package com.shoppingmall.controller;

import com.shoppingmall.domain.item.Item;
import com.shoppingmall.service.user.ItemService;
import com.shoppingmall.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.ItemResponseDto.*;
import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public String home(@Login LoginUserForm loginUserForm, Model model){
        log.info("home access");
        List<Item> items = itemService.searchItems();
        List<ItemInfo> itemInfos = items.stream()
                .map(Item::toItemInfo)
                .collect(Collectors.toList());

        model.addAttribute("user", loginUserForm);
        model.addAttribute("items", itemInfos);

        return "home";
    }
}
