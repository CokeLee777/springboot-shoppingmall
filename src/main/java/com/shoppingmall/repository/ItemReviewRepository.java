package com.shoppingmall.repository;

import com.shoppingmall.domain.review.ItemReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemReviewRepository extends JpaRepository<ItemReview, Long> {
}
