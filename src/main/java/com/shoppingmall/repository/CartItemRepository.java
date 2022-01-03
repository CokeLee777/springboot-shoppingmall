package com.shoppingmall.repository;

import com.shoppingmall.domain.cartitem.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
