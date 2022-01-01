package com.shoppingmall.service.user;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.exception.LoginRequiredException;
import com.shoppingmall.repository.CartRepository;
import com.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

//    public void addItem(String identifier, Item item){
//        User user = userRepository.findByIdentifier(identifier).orElseThrow(
//                () -> new LoginRequiredException("로그인이 필요한 서비스입니다."));
//        user.getCart().addItem(item);
//    }
}
