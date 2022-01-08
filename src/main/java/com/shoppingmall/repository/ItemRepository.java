package com.shoppingmall.repository;

import com.shoppingmall.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * 1. 모든 상품 조회
     * 2. 카테고리별로 조회
     * 공통1. 조회한 후 (1)상품등록 가격 순,(2)상품등록이 3일안으로 나온 새로운 상품순, (3)인기 순으로 정렬
     * 공통2. 조회한 후 상품 10개, 20개, 30개씩 보여주기
     */
    //모든 상품 조회
    Page<Item> findAll(Pageable pageable);

    //카테고리별로 조회
    @Query("select i from Item i inner join i.itemCategory where i.itemCategory.id = :categoryId")
    Page<Item> findItemsJoinCategoryByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    //상품 카테고리와 함께 조회
    @Query("select i from Item i join fetch i.itemCategory where i.id = :itemId")
    Optional<Item> findItemJoinCategory(@Param("itemId") Long id);

    //키워드로 상품 검색
    @Query("select i from Item i where i.name like %:keyword%")
    List<Item> findAllByKeyword(@Param("keyword") String keyword);
}