package com.thaovo.shoppingcart.product.dto;

import lombok.Data;

@Data
public class QuantityBySizeDto {
    private long quantityId;
    private String size;
    private int quantityInStock;
}
