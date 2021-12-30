package com.shoppingmall.repository;

import com.shoppingmall.domain.item.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    Optional<ItemCategory> findByName(String name);
}
