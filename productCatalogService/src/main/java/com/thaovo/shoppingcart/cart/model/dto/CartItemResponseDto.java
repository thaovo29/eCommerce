package com.thaovo.shoppingcart.cart.model.dto;

import com.thaovo.shoppingcart.product.dto.ProductDto;
import lombok.Data;

@Data
public class CartItemResponseDto {
    private ProductDto product;
    private ClassificationDto classification;
    private int amount;
}
