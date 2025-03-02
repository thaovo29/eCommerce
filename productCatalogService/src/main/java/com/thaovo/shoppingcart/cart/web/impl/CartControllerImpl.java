package com.thaovo.shoppingcart.cart.web.impl;

import com.thaovo.shoppingcart.AbstractApplicationController;
import com.thaovo.shoppingcart.cart.model.dto.CartDto;
import com.thaovo.shoppingcart.cart.model.dto.CartItemDto;
import com.thaovo.shoppingcart.cart.model.dto.CartResponseDto;
import com.thaovo.shoppingcart.cart.model.exception.CartItemException;
import com.thaovo.shoppingcart.cart.model.exception.OutOfStockException;
import com.thaovo.shoppingcart.cart.service.CartService;
import com.thaovo.shoppingcart.cart.web.CartController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/cart")
public class CartControllerImpl extends AbstractApplicationController implements CartController {

    private final CartService cartService;

    public CartControllerImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @Override
    public ResponseEntity<CartResponseDto> addToCart(CartItemDto cartItemDto) {
        try {
            return ResponseEntity.ok(cartService.addToCart(cartItemDto));
        } catch (OutOfStockException | CartItemException | NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping
    @Override
    public ResponseEntity<CartResponseDto> updateCart(CartDto cartDto) {
        try {
            return ResponseEntity.ok(cartService.modifyCart(cartDto));
        } catch (OutOfStockException | CartItemException | NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<CartResponseDto> viewCart() {
        try {
            return ResponseEntity.ok(cartService.viewCart());
        } catch (OutOfStockException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{quantityId}")
    @Override
    public ResponseEntity<CartResponseDto> deleteCartItem(Long quantityId) {
        return ResponseEntity.ok(cartService.deleteCartItem(quantityId));
    }
}
