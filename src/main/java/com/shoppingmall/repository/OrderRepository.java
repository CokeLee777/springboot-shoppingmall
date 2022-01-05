package com.shoppingmall.repository;

import com.shoppingmall.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select distinct o from orders o" +
            " join fetch o.user u" +
            " where u.identifier = :identifier")
    List<Order> findAllByIdentifier(String identifier);
}
