package com.thaovo.shoppingcart.cart.service;

import com.thaovo.shoppingcart.cart.model.dto.CartDto;
import com.thaovo.shoppingcart.cart.model.dto.CartItemDto;
import com.thaovo.shoppingcart.cart.model.dto.CartResponseDto;

public interface CartService {
    CartResponseDto viewCart();

    CartResponseDto addToCart(CartItemDto cartItemDto);

    CartResponseDto modifyCart(CartDto cartDto);

    CartResponseDto deleteCartItem(Long quantityId);

    void clearCart();
}
