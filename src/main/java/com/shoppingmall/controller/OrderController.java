package com.shoppingmall.controller;

import com.shoppingmall.dto.CartItemResponseDto;
import com.shoppingmall.service.user.CartService;
import com.shoppingmall.service.user.OrderService;
import com.shoppingmall.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.shoppingmall.dto.CartItemResponseDto.*;
import static com.shoppingmall.dto.CartResponseDto.*;
import static com.shoppingmall.dto.OrderResponseDto.*;
import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    private LoginUserForm addSessionAttribute(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        LoginUserForm loginUserForm = (LoginUserForm) session.getAttribute(SessionConst.LOGIN_USER);
        model.addAttribute("user", loginUserForm);

        return loginUserForm;
    }

    @GetMapping("/order")
    public String order(HttpServletRequest request, Model model){
        LoginUserForm loginUserForm = addSessionAttribute(request, model);
        //장바구니 검색
        CartInfo cartInfo = cartService.searchCart(loginUserForm.getIdentifier());
        //주문 하기
        OrderInfo orderInfo = orderService.order(cartInfo.getId());

        for(CartItemInfo cartItemInfo : cartInfo.getCartItemInfos() ){
            cartService.removeItemFromCart(loginUserForm.getIdentifier(), cartItemInfo.getId());
        }
        model.addAttribute("orderInfo", orderInfo);

        return "order/orderDetails";
    }
}
