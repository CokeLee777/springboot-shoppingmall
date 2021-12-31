package com.shoppingmall.repository;

import com.shoppingmall.domain.user.User;
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
}

