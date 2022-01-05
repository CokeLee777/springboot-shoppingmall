package com.shoppingmall.controller;

import com.shoppingmall.domain.Item;
import com.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.ItemResponseDto.ItemInfo;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model){
        log.info("home access");
        List<Item> items = itemService.searchItems();
        List<ItemInfo> itemInfos = items.stream()
                .map(Item::toItemInfo)
                .collect(Collectors.toList());

        model.addAttribute("items", itemInfos);

        return "home";
    }
}
