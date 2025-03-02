package com.thaovo.shoppingcart.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassifyClotheDto {
    private String color;
    private List<String> images;
    private List<QuantityBySizeDto> quantities;
}
