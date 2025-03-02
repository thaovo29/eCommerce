package com.thaovo.shoppingcart.cart.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CartResponseDto {
    private Set<CartItemResponseDto> items;

    public CartResponseDto() {
    }

    public CartResponseDto(Set<CartItemResponseDto> items) {
        this.items = items;
    }
}
