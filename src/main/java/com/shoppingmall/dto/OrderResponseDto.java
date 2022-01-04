package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.DeliveryStatus;
import com.shoppingmall.domain.enums.OrderStatus;
import com.shoppingmall.domain.user.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.shoppingmall.dto.OrderItemResponseDto.*;

public class OrderResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderInfo {
        private String orderNumber;
        private Address address;
        private OrderStatus orderStatus;
        private DeliveryStatus deliveryStatus;
        private Integer deliveryPrice;
        private Integer totalPrice;
        private List<OrderItemInfo> orderItemInfos;
    }
}
