package com.shoppingmall.service.user;

import com.shoppingmall.domain.cart.Cart;
import com.shoppingmall.domain.cartitem.CartItem;
import com.shoppingmall.domain.item.Item;
import com.shoppingmall.domain.user.User;
import com.shoppingmall.exception.LoginRequiredException;
import com.shoppingmall.exception.NotExistCartItemException;
import com.shoppingmall.exception.NotExistItemException;
import com.shoppingmall.repository.CartItemRepository;
import com.shoppingmall.repository.CartRepository;
import com.shoppingmall.repository.ItemRepository;
import com.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shoppingmall.dto.CartItemResponseDto.*;
import static com.shoppingmall.dto.CartResponseDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    //장바구니에 상품 담기
    @Transactional
    public void addItemToCart(String identifier, Long itemId, Integer count){
        //회원 정보, 상품 정보 조회
        User user = userRepository.findWithCartByIdentifier(identifier).orElseThrow(
                () -> new LoginRequiredException("로그인이 필요한 서비스입니다."));
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotExistItemException("존재하지 않는 상품입니다."));

        //장바구니 상품 생성
        CartItem cartItem = CartItem.builder()
                .item(item)
                .itemPrice(item.getPrice())
                .itemCount(count)
                .build();

        //사용자의 장바구니에 상품 추가
        Cart cart = user.getCart();
        cart.addCartItem(cartItem);
    }

    //장바구니에서 상품 제거
    @Transactional
    public void removeItemFromCart(String identifier, Long cartItemId){
        User user = userRepository.findWithCartByIdentifier(identifier).orElseThrow(
                () -> new LoginRequiredException("로그인이 필요한 서비스입니다."));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new NotExistCartItemException("존재하지 않는 장바구니 상품입니다."));
        //장바구니에서 담은 상품 제거
        Cart cart = user.getCart();
        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }

    //장바구니 조회
    public CartInfo searchCart(String identifier){
        User user = userRepository.findWithCartByIdentifier(identifier).orElseThrow(
                () -> new LoginRequiredException("로그인이 필요한 서비스입니다."));

        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItemInfo> cartItemInfos = cartItems.stream()
                .map(CartItem::toCartItemInfo)
                .collect(Collectors.toList());

        return cart.toCartInfo(cartItemInfos);
    }
}
