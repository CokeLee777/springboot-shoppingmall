package com.shoppingmall.controller;

import com.shoppingmall.service.user.CartService;
import com.shoppingmall.web.SessionConst;
import com.shoppingmall.web.argumentresolver.UserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.shoppingmall.dto.CartItemResponseDto.*;
import static com.shoppingmall.dto.CartResponseDto.*;
import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private LoginUserForm addSessionAttribute(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        LoginUserForm loginUserForm = (LoginUserForm) session.getAttribute(SessionConst.LOGIN_USER);
        model.addAttribute("user", loginUserForm);

        return loginUserForm;
    }

    @UserLogin
    @PostMapping("/cart/{itemId}/add")
    public String addItemToCart(
            HttpServletRequest request,
            @RequestParam("quantity") Integer quantity,
            @PathVariable("itemId") Long itemId, Model model){

        LoginUserForm loginUserForm = addSessionAttribute(request, model);
        cartService.addItemToCart(loginUserForm.getIdentifier(), itemId, quantity);

        log.info("장바구니에 상품 추가 itemId={}", itemId);
        return "redirect:/";
    }

    @UserLogin
    @GetMapping("/cart")
    public String cartList(
            HttpServletRequest request, Model model){

        LoginUserForm loginUserForm = addSessionAttribute(request, model);

        CartInfo cartInfo = cartService.searchCart(loginUserForm.getIdentifier());
        List<CartItemInfo> cartItemInfos = cartInfo.getCartItemInfos();

        model.addAttribute("cartItems", cartItemInfos);
        model.addAttribute("carts", cartInfo);

        log.info("장바구니 access");
        return "cart/cartList";
    }

    @UserLogin
    @GetMapping("/cart/{cartItemId}/delete")
    public String removeCartItem(
            HttpServletRequest request,
            @PathVariable("cartItemId") Long cartItemId, Model model){

        LoginUserForm loginUserForm = addSessionAttribute(request, model);
        cartService.removeItemFromCart(loginUserForm.getIdentifier(), cartItemId);

        log.info("장바구니 상품 삭제 cartItemId={}", cartItemId);
        return "redirect:/cart";
    }
}
