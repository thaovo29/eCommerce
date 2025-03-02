package com.thaovo.shoppingcart.cart.web;

import com.thaovo.shoppingcart.cart.model.dto.CartDto;
import com.thaovo.shoppingcart.cart.model.dto.CartItemDto;
import com.thaovo.shoppingcart.cart.model.dto.CartResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CartController {

    ResponseEntity<CartResponseDto> addToCart(@RequestBody CartItemDto cartItemDto);

    ResponseEntity<CartResponseDto> updateCart(@RequestBody CartDto cartDto);

    ResponseEntity<CartResponseDto> viewCart();

    ResponseEntity<CartResponseDto> deleteCartItem(@PathVariable Long quantityId);

}
