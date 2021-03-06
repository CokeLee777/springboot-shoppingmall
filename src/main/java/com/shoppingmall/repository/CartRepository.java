package com.shoppingmall.repository;

import com.shoppingmall.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c" +
            " join fetch c.user u" +
            " join fetch c.cartItems ci" +
            " join fetch ci.item i" +
            " where c.id = :cartId")
    Optional<Cart> findAllById(@Param("cartId") Long cartId);
}
