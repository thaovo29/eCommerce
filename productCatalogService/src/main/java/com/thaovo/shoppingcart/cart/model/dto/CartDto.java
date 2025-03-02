package com.thaovo.shoppingcart.cart.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CartDto {
    private Set<CartItemDto> items;
}
