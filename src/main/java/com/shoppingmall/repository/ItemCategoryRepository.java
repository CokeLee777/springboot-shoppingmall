package com.shoppingmall.repository;

import com.shoppingmall.domain.item.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    //이름으로 카테고리 찾기
    Optional<ItemCategory> findByName(String name);
    //이름으로 카테고리 존재 여부 찾기
    boolean existsByName(String name);
}
