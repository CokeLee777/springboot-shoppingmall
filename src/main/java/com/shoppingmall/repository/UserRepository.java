package com.shoppingmall.repository;

import com.shoppingmall.domain.user.User;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //아이디로 회원 찾기
    Optional<User> findByIdentifier(String identifier);
    //아이디로 회원 존재 여부 찾기
    boolean existsByIdentifier(String identifier);
    //장바구니와 회원 함께 조회
    @Query("select u from User u" +
            " join fetch u.cart c" +
            " where u.identifier = :identifier")
    Optional<User> findWithCartByIdentifier(String identifier);

    @Query("select u from User u" +
            " join fetch u.cart c" +
            " join fetch c.cartItems ci" +
            " where u.identifier = :identifier")
    Optional<User> findWithCartAndCartItemByUser(String identifier);
}

