package com.shoppingmall.controller;

import com.shoppingmall.service.CartService;
import com.shoppingmall.service.OrderService;
import com.shoppingmall.web.SessionConst;
import com.shoppingmall.web.argumentresolver.UserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.shoppingmall.dto.CartResponseDto.CartInfo;
import static com.shoppingmall.dto.OrderResponseDto.OrderInfo;
import static com.shoppingmall.dto.OrderResponseDto.OrderListInfo;
import static com.shoppingmall.dto.UserRequestDto.LoginUserForm;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    private LoginUserForm addSessionAttribute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (LoginUserForm) session.getAttribute(SessionConst.LOGIN_USER);
    }

    //주문 하기
    @UserLogin
    @GetMapping("/order")
    public String order(HttpServletRequest request, Model model){
        LoginUserForm loginUserForm = addSessionAttribute(request);
        //장바구니 검색
        CartInfo cartInfo = cartService.searchCart(loginUserForm.getIdentifier());
        //주문 하기
        OrderInfo orderInfo = orderService.order(cartInfo.getId());
        //장바구니에 담겨있는 상품 모두 지우기
        cartService.clearItemFromCart(loginUserForm.getIdentifier());

        model.addAttribute("orderInfo", orderInfo);

        log.info("주문 완료 orderNumber={}", orderInfo.getOrderNumber());
        return "order/orderDetails";
    }

    @UserLogin
    @GetMapping("/orders")
    public String orderList(HttpServletRequest request, Model model){
        LoginUserForm loginUserForm = addSessionAttribute(request);
        //주문내역 모두 조회
        List<OrderListInfo> orderListInfos = orderService.searchOrders(loginUserForm.getIdentifier());

        model.addAttribute("orderListInfos", orderListInfos);

        log.info("주문내역 조회 identifier={}", loginUserForm.getIdentifier());
        return "order/orderList";
    }
}
