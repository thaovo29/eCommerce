package com.thaovo.shoppingcart.product.dto;

import lombok.Data;

@Data
public class StatisticProductDto {
    private long countCustomer;
    private long countProduct;
    private long countOrder;
    private long earning;
}
