package com.shoppingmall.service;

import com.shoppingmall.domain.*;
import com.shoppingmall.domain.enums.DeliveryStatus;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.exception.NotExistCartException;
import com.shoppingmall.repository.CartRepository;
import com.shoppingmall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.OrderItemResponseDto.OrderItemInfo;
import static com.shoppingmall.dto.OrderResponseDto.OrderInfo;
import static com.shoppingmall.dto.OrderResponseDto.OrderListInfo;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Transactional
    public OrderInfo order(Long cartId){
        //장바구니 조회
        Cart cart = cartRepository.findAllById(cartId).orElseThrow(
                () -> new NotExistCartException("존재하지 않는 장바구니입니다."));
        List<CartItem> cartItems = cart.getCartItems();
        //배송정보 생성
        Delivery delivery = Delivery.builder()
                .address(cart.getUser().getAddress())
                .deliveryStatus(DeliveryStatus.READY)
                .build();
        //주문 상품 생성
        List<OrderItem> orderItems = cartItems.stream()
                .map(ci -> OrderItem.builder()
                        .item(ci.getItem())
                        .orderPrice(ci.getItemPrice())
                        .count(ci.getItemCount())
                        .build())
                .collect(Collectors.toList());
        List<OrderItemInfo> orderItemInfos = orderItems.stream()
                .map(oi -> oi.toOrderItemInfo(oi.getItem().toItemInfo()))
                .collect(Collectors.toList());
        //주문 생성
        String orderNumber = UUID.randomUUID().toString();
        Order order = Order.builder()
                .user(cart.getUser())
                .orderItems(orderItems)
                .delivery(delivery)
                .orderStatus(OrderStatus.ORDER)
                .orderNumber(orderNumber)
                .build();
        //주문 저장
        Order savedOrder = orderRepository.save(order);

        return savedOrder.toOrderInfo(orderItemInfos);
    }

    //나의 주문 검색
    public List<OrderListInfo> searchOrders(String identifier) {
        //나의 모든 주문 검색
        List<Order> orders = orderRepository.findAllByIdentifier(identifier);
        return orders.stream()
                .map(o -> OrderListInfo.builder()
                        .createdDate(o.getCreatedDate())
                        .orderNumber(o.getOrderNumber())
                        .orderStatus(o.getOrderStatus())
                        .deliveryStatus(o.getDelivery().getDeliveryStatus())
                        .totalPrice(o.getTotalPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
