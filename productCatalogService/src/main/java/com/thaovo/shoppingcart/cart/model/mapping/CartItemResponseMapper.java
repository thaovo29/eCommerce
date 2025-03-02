package com.thaovo.shoppingcart.cart.model.mapping;

import com.thaovo.shoppingcart.IMapper;
import com.thaovo.shoppingcart.cart.model.dto.CartItemResponseDto;
import com.thaovo.shoppingcart.cart.model.dto.ClassificationDto;
import com.thaovo.shoppingcart.cart.model.entity.CartItem;
import com.thaovo.shoppingcart.product.mapping.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartItemResponseMapper implements IMapper<CartItem, CartItemResponseDto> {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public CartItem toEntity(CartItemResponseDto cartItemResponseDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CartItem> toEntities(List<CartItemResponseDto> cartItemResponseDtos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CartItemResponseDto toDTO(CartItem cartItem) {
        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProduct(productMapper.toDTO(cartItem.getQuantity().getProduct()));
        cartItemResponseDto.setAmount(cartItem.getAmount());

        ClassificationDto classificationDto = new ClassificationDto();
        classificationDto.setQuantityId(cartItem.getQuantity().getId());
        classificationDto.setColorName(cartItem.getQuantity().getColor().getColorName());
        classificationDto.setSizeName(cartItem.getQuantity().getSize().getSizeName());
        cartItemResponseDto.setClassification(classificationDto);
        return cartItemResponseDto;

    }

    @Override
    public List<CartItemResponseDto> toDTOs(List<CartItem> entities) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
