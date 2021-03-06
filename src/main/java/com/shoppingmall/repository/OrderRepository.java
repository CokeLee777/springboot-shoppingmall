package com.shoppingmall.repository;

import com.shoppingmall.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select distinct o from Order o" +
            " join fetch o.user u" +
            " where u.identifier = :identifier")
    List<Order> findAllByIdentifier(@Param("identifier") String identifier);
}
